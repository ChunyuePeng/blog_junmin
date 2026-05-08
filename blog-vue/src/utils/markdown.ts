import { marked } from "marked";
import hljs from "highlight.js";

// ================= TOC 工厂（避免全局污染） =================
function createToc() {
  return {
    toc: [] as any[],
    index: 0,

    add(text: string, level: number) {
      const anchor = `toc${level}-${++this.index}`;
      this.toc.push({ anchor, level, text });
      return anchor;
    },

    toHTML() {
      if (!this.toc.length) return "";

      const html = this.toc
          .map(
              (item) =>
                  `<li><a class="toc-link" href="#${item.anchor}">${item.text}</a></li>`
          )
          .join("");

      this.toc = [];
      this.index = 0;

      return `<ul class="anchor-ul">${html}</ul>`;
    },
  };
}

// ================= Markdown 解析器 =================
class MarkUtils {
  private toc = createToc();

  parse(data: string) {
    if (!data) {
      return { content: "", toc: "" };
    }

    // marked v5 推荐写法（稳定）
    const html = marked.parse(data, {
      async: false,
      gfm: true,
      breaks: false,

      renderer: {
        heading: (token: any) => {
          const anchor = this.toc.add(token.text, token.depth);
          return `<h${token.depth} id="${anchor}">${token.text}</h${token.depth}>`;
        },

        table: (header: string, body: string) => {
          return `
            <table class="table" border="0" cellspacing="0" cellpadding="0">
              ${header}
              ${body}
            </table>
          `;
        },
      },

      highlight: (code: string) => {
        return hljs.highlightAuto(code).value;
      },
    });

    return {
      content: typeof html === "string" ? html : String(html),
      toc: this.toc.toHTML(),
    };
  }
}

const markdown = new MarkUtils();
export default markdown;