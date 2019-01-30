//@flow
import React from "react";
import { translate } from "react-i18next";
import { CreateButtonInBox, CreateButton } from "@scm-manager/ui-components";

// TODO remove
type Props = {
  t: string => string,
  showButtonInBox: boolean
};

class CreateUserButton extends React.Component<Props> {
  render() {
    const { showButtonInBox, t } = this.props;
    if (!showButtonInBox) {
      return (
        <CreateButton label={t("createUserButton.label")} link="/users/add" />
      );
    }

    return (
      <CreateButtonInBox
        label={t("createUserButton.label")}
        link="/users/add"
      />
    );
  }
}

export default translate("users")(CreateUserButton);
