//@flow
import React from "react";
import SyntaxHighlighter from "./SyntaxHighlighter";
import Markdown from "react-markdown/with-html";
import { binder } from "@scm-manager/ui-extensions";

type Props = {
  content: string,
  renderContext?: Object,
  renderers?: Object
};

class MarkdownView extends React.Component<Props> {
  flatten(text: string, child: any) {
    return typeof child === "string"
      ? text + child
      : React.Children.toArray(child.props.children).reduce(this.flatten, text);
  }

  HeadingRenderer(props: any) {
    var children = React.Children.toArray(props.children);
    var text = children.reduce(this.flatten, "");
    var slug = text.toLowerCase().replace(/\W/g, "-");
    return React.createElement("h" + props.level, { id: slug }, props.children);
  }

  render() {
    const { content, renderers, renderContext } = this.props;

    const rendererFactory = binder.getExtension("markdown-renderer-factory");
    let rendererList = renderers;

    if (rendererFactory) {
      rendererList = rendererFactory(renderContext);
    }

    if (!rendererList) {
      rendererList = {};
    }

    if (!rendererList.code) {
      rendererList.code = SyntaxHighlighter;
    }
    if (!rendererList.heading) {
      rendererList.heading = this.HeadingRenderer;
    }

    return (
      <Markdown
        className="content"
        skipHtml={true}
        escapeHtml={true}
        source={content}
        renderers={rendererList}
      />
    );
  }
}

export default MarkdownView;
