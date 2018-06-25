package sonia.scm.api.v2.resources;

import com.fasterxml.jackson.databind.node.TextNode;
import de.otto.edison.hal.HalRepresentation;
import org.junit.Test;
import org.mapstruct.factory.Mappers;
import sonia.scm.group.Group;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class GroupDtoToGroupMapperTest {

  @Test
  public void shouldMapAttributes() {
    GroupDto dto = new GroupDto();
    dto.setName("group");
    Group group = Mappers.getMapper(GroupDtoToGroupMapper.class).map(dto);
    assertEquals("group", group.getName());
  }

  @Test
  public void shouldMapMembers() {
    GroupDto dto = new GroupDto();

    HalRepresentation member1 = new HalRepresentation();
    member1.getAttributes().put("name", new TextNode("member1"));
    HalRepresentation member2 = new HalRepresentation();
    member2.getAttributes().put("name", new TextNode("member2"));

    dto.withEmbedded("members", asList(member1, member2));

    Group group = Mappers.getMapper(GroupDtoToGroupMapper.class).map(dto);

    assertEquals(2, group.getMembers().size());
    assertEquals("member1", group.getMembers().get(0));
    assertEquals("member2", group.getMembers().get(1));
  }
}