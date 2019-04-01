//@flow
import React from "react";
import type { Repository, Branch } from "@scm-manager/ui-types";
import { ButtonGroup, Button } from "@scm-manager/ui-components";
import { translate } from "react-i18next";
import { createChangesetLink, createSourcesLink } from "../../modules/branches";

type Props = {
  repository: Repository,
  branch: Branch,

  // context props
  t: string => string
};

class BranchButtonGroup extends React.Component<Props> {
  render() {
    const { repository, branch, t } = this.props;

    const changesetLink = createChangesetLink(repository, branch);
    const sourcesLink = createSourcesLink(repository, branch);

    return (
      <ButtonGroup>
        <Button link={changesetLink}>
          <span className="icon">
            <i className="fas fa-exchange-alt" />
          </span>
          <span className="is-hidden-mobile is-hidden-tablet-only">
            {t("branch.commits")}
          </span>
        </Button>
        <Button link={sourcesLink}>
          <span className="icon">
            <i className="fas fa-code" />
          </span>
          <span className="is-hidden-mobile is-hidden-tablet-only">
            {t("branch.sources")}
          </span>
        </Button>
      </ButtonGroup>
    );
  }
}

export default translate("repos")(BranchButtonGroup);