//@flow
import React from "react";
import type { Links } from "@scm-manager/ui-types";
import { translate } from "react-i18next";
import { InputField, Checkbox, Select } from "@scm-manager/ui-components";

type Configuration = {
  repositoryDirectory: string,
  compatibility: string,
  enabledGZip: boolean,
  disabled: boolean,
  _links: Links
};

type Props = {
  initialConfiguration: Configuration,
  readOnly: boolean,

  onConfigurationChange: (Configuration, boolean) => void,

  // context props
  t: (string) => string
}

type State = Configuration;

class HgConfigurationForm extends React.Component<Props, State> {

  constructor(props: Props) {
    super(props);
    this.state = { ...props.initialConfiguration, validationErrors: [] };
  }

  isValid = () => {
    return !!this.state.repositoryDirectory;
  };

  handleChange = (value: any, name: string) => {
    this.setState({
      [name]: value
    }, () => this.props.onConfigurationChange(this.state, this.isValid()));
  };

  compatibilityOptions = (values: string[]) => {
    const options = [];
    for (let value of values) {
      options.push( this.compatibilityOption(value) );
    }
    return options;
  };

  compatibilityOption = (value: string) => {
    return {
      value,
      label: this.props.t("scm-svn-plugin.config.compatibility-values." + value.toLowerCase())
    };
  };

  render() {
    const { readOnly, t } = this.props;
    const compatibilityOptions = this.compatibilityOptions([
      "NONE", "PRE14", "PRE15", "PRE16", "PRE17", "WITH17"
    ]);

    return (
      <>
        <InputField
          name="repositoryDirectory"
          label={t("scm-svn-plugin.config.directory")}
          helpText={t("scm-svn-plugin.config.directoryHelpText")}
          value={this.state.repositoryDirectory}
          errorMessage={t("scm-svn-plugin.config.required")}
          validationError={!this.state.repositoryDirectory}
          onChange={this.handleChange}
          disabled={readOnly}
        />
        <Select
          name="compatibility"
          label={t("scm-svn-plugin.config.compatibility")}
          helpText={t("scm-svn-plugin.config.compatibilityHelpText")}
          value={this.state.compatibility}
          options={compatibilityOptions}
          onChange={this.handleChange}
        />
        <Checkbox
          name="enabledGZip"
          label={t("scm-svn-plugin.config.enabledGZip")}
          helpText={t("scm-svn-plugin.config.enabledGZipHelpText")}
          checked={this.state.enabledGZip}
          onChange={this.handleChange}
          disabled={readOnly}
        />
        <Checkbox
          name="disabled"
          label={t("scm-svn-plugin.config.disabled")}
          helpText={t("scm-svn-plugin.config.disabledHelpText")}
          checked={this.state.disabled}
          onChange={this.handleChange}
          disabled={readOnly}
        />
      </>
    );
  }

}

export default translate("plugins")(HgConfigurationForm);
