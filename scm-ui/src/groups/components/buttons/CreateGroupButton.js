//@flow
import React from "react";
import { translate } from "react-i18next";
import { CreateButton, CreateButtonInBox } from "@scm-manager/ui-components";

type Props = {
  t: string => string,
  showButtonInBox: boolean
};

class CreateGroupButton extends React.Component<Props> {
  render() {
    const { showButtonInBox, t } = this.props;

    if (!showButtonInBox) {
      return (
        <CreateButton label={t("createGroupButton.label")} link="/groups/add" />
      );
    }

    return (
      <CreateButtonInBox
        label={t("createGroupButton.label")}
        link="/groups/add"
      />
    );
  }
}

export default translate("groups")(CreateGroupButton);
