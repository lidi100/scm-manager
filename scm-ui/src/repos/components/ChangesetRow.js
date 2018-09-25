//@flow
import React from "react";
import type { Changeset, Repository } from "@scm-manager/ui-types";
import classNames from "classnames";
import { translate, Interpolate } from "react-i18next";
import ChangesetAvatar from "./ChangesetAvatar";
import ChangesetId from "./ChangesetId";
import injectSheet from "react-jss";
import { DateFromNow } from "@scm-manager/ui-components";
import ChangesetAuthor from "./ChangesetAuthor";

const styles = {
  pointer: {
    cursor: "pointer"
  },
  changesetGroup: {
    marginBottom: "1em"
  },
  withOverflow: {
    overflow: "auto"
  }
};

type Props = {
  repository: Repository,
  changeset: Changeset,
  t: any,
  classes: any
};

class ChangesetRow extends React.Component<Props> {
  createLink = (changeset: Changeset) => {
    const { repository } = this.props;
    return <ChangesetId changeset={changeset} repository={repository} />;
  };

  render() {
    const { changeset, classes } = this.props;
    const changesetLink = this.createLink(changeset);
    const dateFromNow = <DateFromNow date={changeset.date} />;
    const authorLine = <ChangesetAuthor changeset={changeset}/>;
    return (
      <article className={classNames("media", classes.inner)}>
        <figure className="media-left">
          <ChangesetAvatar changeset={changeset} />
        </figure>
        <div className={classNames("media-content", classes.withOverflow)}>
          <div className="content">
            <p className="is-ellipsis-overflow">
              {changeset.description}
              <br />
              <Interpolate
                i18nKey="changeset.summary"
                id={changesetLink}
                time={dateFromNow}
              />
            </p>{" "}
            <p className="is-size-7">{authorLine}</p>
          </div>
        </div>
      </article>
    );
  }
}

export default injectSheet(styles)(translate("changesets")(ChangesetRow));