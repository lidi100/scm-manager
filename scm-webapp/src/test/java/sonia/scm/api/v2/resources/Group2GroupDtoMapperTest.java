package sonia.scm.api.v2.resources;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.SubjectThreadState;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.util.ThreadState;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mapstruct.factory.Mappers;
import sonia.scm.group.Group;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class Group2GroupDtoMapperTest {

  private final Group2GroupDtoMapper mapper = Mappers.getMapper(Group2GroupDtoMapper.class);
  private final UriInfo uriInfo = mock(UriInfo.class);
  private final Subject subject = mock(Subject.class);
  private final ThreadState subjectThreadState = new SubjectThreadState(subject);

  private URI expectedBaseUri;

  @Before
  public void init() throws URISyntaxException {
    URI baseUri = new URI("http://example.com/base/");
    expectedBaseUri = baseUri.resolve(GroupV2Resource.GROUPS_PATH_V2 + "/");
    when(uriInfo.getBaseUri()).thenReturn(baseUri);
    subjectThreadState.bind();
    ThreadContext.bind(subject);
  }

  @After
  public void unbindSubject() {
    ThreadContext.unbindSubject();
  }

  @Test
  public void shouldMapAttributes() {
    Group group = createDefaultGroup();

    GroupDto groupDto = mapper.groupToGroupDto(group, uriInfo);

    assertEquals("abc", groupDto.getName());
    assertEquals("abc", groupDto.getName());
  }

  @Test
  public void shouldMapSelfLink() {
    Group group = createDefaultGroup();

    GroupDto groupDto = mapper.groupToGroupDto(group, uriInfo);

    assertEquals("expected self link", expectedBaseUri.resolve("abc").toString(), groupDto.getLinks().getLinkBy("self").get().getHref());
  }

  @Test
  public void shouldMapLink_forUpdate() {
    Group group = createDefaultGroup();
    when(subject.isPermitted("group:modify:abc")).thenReturn(true);

    GroupDto groupDto = mapper.groupToGroupDto(group, uriInfo);

    assertEquals("expected update link", expectedBaseUri.resolve("abc").toString(), groupDto.getLinks().getLinkBy("update").get().getHref());
  }

  @Test
  public void shouldCreateMemberDtos() {
    Group group = createDefaultGroup();
    group.setMembers(IntStream.range(0, 10).mapToObj(n -> "user" + n).collect(toList()));

    GroupDto groupDto = mapper.groupToGroupDto(group, uriInfo);

    assertEquals(10, groupDto.getMembers().size());
    MemberDto actualMember = groupDto.getMembers().iterator().next();
    assertEquals("user0", actualMember.getName());
    assertEquals("http://example.com/base/v2/users/user0", actualMember.getLinks().getLinkBy("self").get().getHref());
  }

  private Group createDefaultGroup() {
    Group group = new Group();
    group.setName("abc");
    group.setCreationDate(1L);
    return group;
  }
}
