// @flow
import React from "react";

type Props = {
  className?: string
};

class HorizontalRule extends React.Component<Props> {
  render() {
    const { className } = this.props;
    if (className) {
      return <hr className={className} />;
    }
    return <hr />;
  }
}

export default HorizontalRule;
