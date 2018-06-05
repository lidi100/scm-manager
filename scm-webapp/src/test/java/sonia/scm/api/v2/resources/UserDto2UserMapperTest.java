package sonia.scm.api.v2.resources;

import org.apache.shiro.authc.credential.PasswordService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import sonia.scm.user.User;

import java.time.Instant;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserDto2UserMapperTest {

  @Mock
  private PasswordService passwordService;
  @InjectMocks
  private UserDto2UserMapperImpl mapper;

  @Test
  public void shouldMapFields() {
    UserDto dto = createDefaultDto();
    User user = mapper.userDtoToUser(dto, "original password");
    assertEquals("abc" , user.getName());
  }

  @Test
  public void shouldEncodePassword() {
    when(passwordService.encryptPassword("unencrypted")).thenReturn("encrypted");

    UserDto dto = createDefaultDto();
    dto.setPassword("unencrypted");
    User user = mapper.userDtoToUser(dto, "original password");
    assertEquals("encrypted" , user.getPassword());
  }

  @Test
  public void shouldMapTimes() {
    UserDto dto = createDefaultDto();
    Instant expectedCreationDate = Instant.ofEpochMilli(66666660000L);
    Optional<Instant> expectedModificationDate = Optional.empty();
    dto.setCreationDate(expectedCreationDate);
    dto.setLastModified(expectedModificationDate);

    User user = mapper.userDtoToUser(dto, "original password");

    assertEquals((Long) expectedCreationDate.toEpochMilli(), user.getCreationDate());
    assertNull(user.getLastModified());
  }

  @Before
  public void init() {
    initMocks(this);
  }

  private UserDto createDefaultDto() {
    UserDto dto = new UserDto();
    dto.setName("abc");
    dto.setCreationDate(Instant.now());
    dto.setLastModified(Optional.empty());
    return dto;
  }
}