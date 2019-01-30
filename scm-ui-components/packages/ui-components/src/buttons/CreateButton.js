//@flow
import React from "react";
import SubmitButton, { type ButtonProps } from "./SubmitButton";


class CreateButton extends React.Component<ButtonProps> {
  render() {
    return (
        <SubmitButton {...this.props} />
    );
  }
}

export default CreateButton;
