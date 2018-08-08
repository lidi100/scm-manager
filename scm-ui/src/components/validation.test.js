// @flow
import * as validator from "./validation";

describe("test name validation", () => {
  it("should return false", () => {
    // invalid names taken from ValidationUtilTest.java
    const invalidNames = [
      " test 123",
      " test 123 ",
      "test 123 ",
      "test/123",
      "test%123",
      "test:123",
      "t ",
      " t",
      " t ",
      "",

      " invalid_name",
      "another%one",
      "!!!",
      "!_!"
    ];
    for (let name of invalidNames) {
      expect(validator.isNameValid(name)).toBe(false);
    }
  });

  it("should return true", () => {
    // valid names taken from ValidationUtilTest.java
    const validNames = [
      "test",
      "test.git",
      "Test123.git",
      "Test123-git",
      "Test_user-123.git",
      "test@scm-manager.de",
      "test 123",
      "tt",
      "t",

      "valid_name",
      "another1",
      "stillValid",
      "this.one_as-well",
      "and@this"
    ];
    for (let name of validNames) {
      expect(validator.isNameValid(name)).toBe(true);
    }
  });
});

describe("test mail validation", () => {
  it("should return false", () => {
    // invalid taken from ValidationUtilTest.java
    const invalid = [
      "ostfalia.de",
      "@ostfalia.de",
      "s.sdorra@",
      "s.sdorra@ostfalia",
      "s.sdorra@@ostfalia.de",
      "s.sdorra@ ostfalia.de",
      "s.sdorra @ostfalia.de"
    ];
    for (let mail of invalid) {
      expect(validator.isMailValid(mail)).toBe(false);
    }
  });

  it("should return true", () => {
    // valid taken from ValidationUtilTest.java
    const valid = [
      "s.sdorra@ostfalia.de",
      "sdorra@ostfalia.de",
      "s.sdorra@hbk-bs.de",
      "s.sdorra@gmail.com",
      "s.sdorra@t.co",
      "s.sdorra@ucla.college",
      "s.sdorra@example.xn--p1ai",
      "s.sdorra@scm.solutions"
    ];
    for (let mail of valid) {
      expect(validator.isMailValid(mail)).toBe(true);
    }
  });
});
