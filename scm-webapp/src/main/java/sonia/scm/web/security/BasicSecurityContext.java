/**
 * Copyright (c) 2010, Sebastian Sdorra
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of SCM-Manager; nor the names of its
 *    contributors may be used to endorse or promote products derived from this
 *    software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * http://bitbucket.org/sdorra/scm-manager
 *
 */



package sonia.scm.web.security;

//~--- non-JDK imports --------------------------------------------------------

import com.google.inject.Inject;
import com.google.inject.servlet.SessionScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sonia.scm.config.ScmConfiguration;
import sonia.scm.group.Group;
import sonia.scm.group.GroupManager;
import sonia.scm.user.User;
import sonia.scm.user.UserManager;

//~--- JDK imports ------------------------------------------------------------

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Sebastian Sdorra
 */
@SessionScoped
public class BasicSecurityContext implements WebSecurityContext
{

  /** Field description */
  public static final String USER_ANONYMOUS = "anonymous";

  /** the logger for BasicSecurityContext */
  private static final Logger logger =
    LoggerFactory.getLogger(BasicSecurityContext.class);

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   *
   *
   * @param configuration
   * @param authenticator
   * @param groupManager
   * @param userManager
   */
  @Inject
  public BasicSecurityContext(ScmConfiguration configuration,
                              AuthenticationManager authenticator,
                              GroupManager groupManager,
                              UserManager userManager)
  {
    this.configuration = configuration;
    this.authenticator = authenticator;
    this.groupManager = groupManager;
    this.userManager = userManager;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   * @param username
   * @param password
   *
   * @return
   */
  @Override
  public User authenticate(HttpServletRequest request,
                           HttpServletResponse response, String username,
                           String password)
  {
    user = authenticator.authenticate(request, response, username, password);

    if (user != null)
    {
      try
      {
        user.setLastLogin(System.currentTimeMillis());

        User dbUser = userManager.get(username);

        if (dbUser != null)
        {

          // update properties
          dbUser.setDisplayName(user.getDisplayName());
          dbUser.setLastLogin(user.getLastLogin());
          dbUser.setMail(user.getMail());
          dbUser.setType(user.getType());
          userManager.modify(dbUser);
        }
        else
        {
          userManager.create(user);
        }

        loadGroups();
      }
      catch (Exception ex)
      {
        user = null;
        logger.error(ex.getMessage(), ex);
      }
    }

    return user;
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   */
  @Override
  public void logout(HttpServletRequest request, HttpServletResponse response)
  {
    user = null;
    groups = null;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public Collection<String> getGroups()
  {
    if (groups == null)
    {
      groups = new HashSet<String>();
    }

    return groups;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public User getUser()
  {
    if ((user == null) && configuration.isAnonymousAccessEnabled())
    {
      user = userManager.get(USER_ANONYMOUS);
    }

    return user;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public boolean isAuthenticated()
  {
    return getUser() != null;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   */
  private void loadGroups()
  {
    groups = new HashSet<String>();

    Collection<Group> groupCollection =
      groupManager.getGroupsForMember(user.getName());

    if (groupCollection != null)
    {
      for (Group group : groupCollection)
      {
        groups.add(group.getName());
      }
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private AuthenticationManager authenticator;

  /** Field description */
  private ScmConfiguration configuration;

  /** Field description */
  private GroupManager groupManager;

  /** Field description */
  private Set<String> groups = new HashSet<String>();

  /** Field description */
  private User user;

  /** Field description */
  private UserManager userManager;
}
