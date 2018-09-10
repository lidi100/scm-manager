package sonia.scm.web;

import sonia.scm.api.v2.resources.UriInfoStore;
import sonia.scm.repository.spi.InitializingHttpScmProtocolWrapper;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class GitScmProtocolProviderWrapper extends InitializingHttpScmProtocolWrapper {
  @Inject
  public GitScmProtocolProviderWrapper(Provider<ScmGitServlet> servletProvider, Provider<UriInfoStore> uriInfoStore) {
    super(servletProvider, uriInfoStore);
  }
}
