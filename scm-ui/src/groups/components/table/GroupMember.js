// @flow
import React from "react";
import { Link } from "react-router-dom";
import type { Member } from "../../types/Group";

type Props = {
  member: Member
};

export default class GroupMember extends React.Component<Props> {
  renderLink(to: string, label: string) {
    return <Link to={to}>{label}</Link>;
  }

  showName(to: any, member: Member) {
    if (member._links.self) {
      return this.renderLink(to, member.name);
    } else {
      return member.name;
    }
  }

  render() {
    const { member } = this.props;
    const to = `/user/${member.name}`;
    return <li>{this.showName(to, member)}</li>;
  }
}
