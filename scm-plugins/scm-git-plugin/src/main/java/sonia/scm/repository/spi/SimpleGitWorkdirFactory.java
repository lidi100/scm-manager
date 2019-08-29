package sonia.scm.repository.spi;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.ScmTransportProtocol;
import sonia.scm.repository.GitWorkdirFactory;
import sonia.scm.repository.InternalRepositoryException;
import sonia.scm.repository.util.SimpleWorkdirFactory;
import sonia.scm.repository.util.WorkdirProvider;

import javax.inject.Inject;
import java.io.File;

public class SimpleGitWorkdirFactory extends SimpleWorkdirFactory<Repository, GitContext> implements GitWorkdirFactory {

  @Inject
  public SimpleGitWorkdirFactory(WorkdirProvider workdirProvider) {
    super(workdirProvider);
  }

  @Override
  public ParentAndClone<Repository> cloneRepository(GitContext context, File target) {
    try {
      return new ParentAndClone<>(null, Git.cloneRepository()
        .setURI(createScmTransportProtocolUri(context.getDirectory()))
        .setDirectory(target)
        .call()
        .getRepository());
    } catch (GitAPIException e) {
      throw new InternalRepositoryException(context.getRepository(), "could not clone working copy of repository", e);
    }
  }

  private String createScmTransportProtocolUri(File bareRepository) {
    return ScmTransportProtocol.NAME + "://" + bareRepository.getAbsolutePath();
  }

  @Override
  protected void closeRepository(Repository repository) {
    // we have to check for null here, because we do not create a repository for
    // the parent in cloneRepository
    if (repository != null) {
      repository.close();
    }
  }

  @Override
  protected sonia.scm.repository.Repository getScmRepository(GitContext context) {
    return context.getRepository();
  }
}
