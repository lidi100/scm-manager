// @flow
import React from "react";
import RepositoryRoleForm from "./RepositoryRoleForm";
import { connect } from "react-redux";
import { translate } from "react-i18next";
import {
  getModifyRoleFailure,
  isModifyRolePending,
  modifyRole
} from "../modules/roles";
import { ErrorNotification, Subtitle } from "@scm-manager/ui-components";
import type { RepositoryRole } from "@scm-manager/ui-types";
import type { History } from "history";

type Props = {
  disabled: boolean,
  role: RepositoryRole,
  repositoryRolesLink: string,
  error?: Error,

  // context objects
  t: string => string,
  history: History,

  //dispatch function
  updateRole: (
    link: string,
    role: RepositoryRole,
    callback?: () => void
  ) => void
};

class EditRepositoryRole extends React.Component<Props> {
  repositoryRoleUpdated = (role: RepositoryRole) => {
    const { history } = this.props;
    history.push("/config/roles/");
  };

  updateRepositoryRole = (role: RepositoryRole) => {
    this.props.updateRole(role, () => this.repositoryRoleUpdated(role));
  };

  render() {
    const { error, t } = this.props;

    if (error) {
      return <ErrorNotification error={error} />;
    }

    return (
      <>
        <Subtitle subtitle={t("repositoryRole.editSubtitle")} />
        <RepositoryRoleForm
          role={this.props.role}
          submitForm={role => this.updateRepositoryRole(role)}
        />
      </>
    );
  }
}

const mapStateToProps = (state, ownProps) => {
  const loading = isModifyRolePending(state);
  const error = getModifyRoleFailure(state, ownProps.role.name);

  return {
    loading,
    error
  };
};

const mapDispatchToProps = dispatch => {
  return {
    updateRole: (role: RepositoryRole, callback?: () => void) => {
      dispatch(modifyRole(role, callback));
    }
  };
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(translate("config")(EditRepositoryRole));
