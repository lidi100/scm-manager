package sonia.scm.repository.spi;

import com.aragost.javahg.Repository;
import com.aragost.javahg.commands.CloneCommand;
import com.aragost.javahg.commands.PullCommand;
import sonia.scm.repository.util.SimpleWorkdirFactory;
import sonia.scm.repository.util.WorkdirProvider;
import sonia.scm.web.HgRepositoryEnvironmentBuilder;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.function.BiConsumer;

public class SimpleHgWorkdirFactory extends SimpleWorkdirFactory<Repository, HgCommandContext> implements HgWorkdirFactory {

  private final Provider<HgRepositoryEnvironmentBuilder> hgRepositoryEnvironmentBuilder;

  @Inject
  public SimpleHgWorkdirFactory(Provider<HgRepositoryEnvironmentBuilder> hgRepositoryEnvironmentBuilder, WorkdirProvider workdirProvider) {
    super(workdirProvider);
    this.hgRepositoryEnvironmentBuilder = hgRepositoryEnvironmentBuilder;
  }
  @Override
  public ParentAndClone<Repository> cloneRepository(HgCommandContext context, File target) throws IOException {
    BiConsumer<sonia.scm.repository.Repository, Map<String, String>> repositoryMapBiConsumer =
      (repository, environment) -> hgRepositoryEnvironmentBuilder.get().buildFor(repository, null, environment);
    Repository centralRepository = context.openWithSpecialEnvironment(repositoryMapBiConsumer);
    CloneCommand.on(centralRepository).execute(target.getAbsolutePath());
    return new ParentAndClone<>(centralRepository, Repository.open(target));
  }

  @Override
  protected void closeRepository(Repository repository) {
    repository.close();
  }

  @Override
  protected sonia.scm.repository.Repository getScmRepository(HgCommandContext context) {
    return context.getScmRepository();
  }

  @Override
  public void configure(PullCommand pullCommand) {
    pullCommand.cmdAppend("--config", "hooks.changegroup.scm=python:scmhooks.postHook");
    pullCommand.cmdAppend("--config", "hooks.pretxnchangegroup.scm=python:scmhooks.preHook");
  }
}
