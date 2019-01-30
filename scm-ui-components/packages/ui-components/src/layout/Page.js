//@flow
import * as React from "react";
import Loading from "./../Loading";
import ErrorNotification from "./../ErrorNotification";
import Title from "./Title";
import Subtitle from "./Subtitle";

type Props = {
  title?: string,
  subtitle?: string,
  loading?: boolean,
  error?: Error,
  showContentOnError?: boolean,
  children: React.Node,
  renderLeftOfTitle: () => any
};

class Page extends React.Component<Props> {
  render() {
    const { title, error, subtitle } = this.props;
    return (
      <section className="section">
        <div className="container">
          <div className="level">
            <div className="level-left">
              <div className="level-item">
                <Title title={title} />
              </div>
            </div>
            <div className="level-right">
              <div className="level-item">
                {this.renderButton()}
              </div>
            </div>
          </div>
          {this.renderHorizontalRule()}
          <Subtitle subtitle={subtitle} />
          <ErrorNotification error={error} />
          {this.renderContent()}
        </div>
      </section>
    );
  }

  renderButton() {
    const {loading, renderLeftOfTitle} = this.props;
    if(loading || !renderLeftOfTitle){
      return null;
    }
    return renderLeftOfTitle();
  }

  renderHorizontalRule() {
    const { title, subtitle } = this.props;
    if (title && subtitle) {
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

export default Page;
