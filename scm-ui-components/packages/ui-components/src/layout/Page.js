//@flow
import * as React from "react";
import Loading from "./../Loading";
import ErrorNotification from "./../ErrorNotification";
import Title from "./Title";
import Subtitle from "./Subtitle";
import classNames from "classnames";
import injectSheet from "react-jss";

type Props = {
  title?: string,
  subtitle?: string,
  loading?: boolean,
  error?: Error,
  showContentOnError?: boolean,
  children: React.Node,
  renderLeftOfTitle: () => any,
  classes: any
};

const styles = {
  spacing: {
    marginBottom: "0rem !important"
  }
};

class Page extends React.Component<Props> {
  render() {
    const { title, error, subtitle } = this.props;
    return (
      <section className="section">
        <div className="container">
          {this.renderTitle()}
          <Subtitle subtitle={subtitle} />
          {this.renderHorizontalRule()}
          <ErrorNotification error={error} />
          {this.renderContent()}
        </div>
      </section>
    );
  }

  renderTitle() {
    const { title, renderLeftOfTitle, classes } = this.props;
    if (renderLeftOfTitle) {
      return (
        <>
        <div className={classNames(classes.spacing, "level")}>
          <div className="level-left">
            <div className="level-item">
              <Title title={title} />
            </div>
          </div>
          <div className="level-right">
            <div className="level-item">{this.renderLeftOfTitle()}</div>
          </div>
        </div>
          </>
      );
    }
    return <Title title={title} />;
  }

  renderLeftOfTitle() {
    const { loading, renderLeftOfTitle } = this.props;
    if (loading || !renderLeftOfTitle) {
      return null;
    }
    return renderLeftOfTitle();
  }

  renderHorizontalRule() {
    const { renderLeftOfTitle } = this.props;
    if (renderLeftOfTitle) {
      return <hr className="page" />;
    }
  }

  renderContent() {
    const { loading, children, showContentOnError, error } = this.props;
    if (error && !showContentOnError) {
      return null;
    }
    if (loading) {
      return <Loading />;
    }
    return children;
  }
}

export default injectSheet(styles)(Page);
