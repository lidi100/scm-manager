package sonia.scm.legacy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sonia.scm.migration.MigrationDAO;
import sonia.scm.migration.MigrationInfo;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.util.Collections.singletonList;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RepositoryLegacyProtocolRedirectFilterTest {

  @Mock
  MigrationDAO migrationDAO;
  @Mock
  HttpServletRequest request;
  @Mock
  HttpServletResponse response;
  @Mock
  FilterChain filterChain;

  @BeforeEach
  void initContextPath() {
    lenient().when(request.getContextPath()).thenReturn("/scm");
    lenient().when(request.getQueryString()).thenReturn("");
  }

  @Test
  void shouldNotRedirectForEmptyMigrationList() throws IOException, ServletException {
    when(request.getServletPath()).thenReturn("/git/old/name");

    new RepositoryLegacyProtocolRedirectFilter(migrationDAO).doFilter(request, response, filterChain);

    verify(filterChain).doFilter(request, response);
  }

  @Test
  void shouldRedirectForExistingRepository() throws IOException, ServletException {
    when(migrationDAO.getAll()).thenReturn(singletonList(new MigrationInfo("id", "git", "old/name", "namespace", "name")));
    when(request.getServletPath()).thenReturn("/git/old/name");

    new RepositoryLegacyProtocolRedirectFilter(migrationDAO).doFilter(request, response, filterChain);

    verify(response).sendRedirect("/scm/repo/namespace/name");
    verify(filterChain, never()).doFilter(request, response);
  }

  @Test
  void shouldRedirectForExistingRepositoryWithFurtherPathElements() throws IOException, ServletException {
    when(migrationDAO.getAll()).thenReturn(singletonList(new MigrationInfo("id", "git", "old/name", "namespace", "name")));
    when(request.getServletPath()).thenReturn("/git/old/name/info/refs");

    new RepositoryLegacyProtocolRedirectFilter(migrationDAO).doFilter(request, response, filterChain);

    verify(response).sendRedirect("/scm/repo/namespace/name/info/refs");
    verify(filterChain, never()).doFilter(request, response);
  }

  @Test
  void shouldRedirectWithQueryParameters() throws IOException, ServletException {
    when(migrationDAO.getAll()).thenReturn(singletonList(new MigrationInfo("id", "git", "old/name", "namespace", "name")));
    when(request.getServletPath()).thenReturn("/git/old/name/info/refs");
    when(request.getQueryString()).thenReturn("parameter=value");

    new RepositoryLegacyProtocolRedirectFilter(migrationDAO).doFilter(request, response, filterChain);

    verify(response).sendRedirect("/scm/repo/namespace/name/info/refs?parameter=value");
    verify(filterChain, never()).doFilter(request, response);
  }
}
