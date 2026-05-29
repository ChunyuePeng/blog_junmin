package org.example.junmin.util;

import com.hankcs.hanlp.HanLP;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Document;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.sequence.BasedSequence;
import com.vladsch.flexmark.util.ast.NodeVisitor;
import com.vladsch.flexmark.util.ast.VisitHandler;
import com.vladsch.flexmark.ast.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MarkdownSegmentUtil {
    private static final Parser PARSER = Parser.builder().build();

    /**
     * Markdown -> 分词结果
     */
    public static List<String> segment(String markdown) {

        // 1. 解析 AST
        Node document = PARSER.parse(markdown);

        // 2. 提取纯文本
        List<String> texts = new ArrayList<>();
        extractText(document, texts);

        String plainText = String.join(" ", texts);

        // 3. 清洗文本
        plainText = cleanText(plainText);

        // 4. 中文分词
        return HanLP.segment(plainText)
                .stream()
                .map(term -> term.word)
                .filter(w -> w != null && !w.trim().isEmpty())
                .collect(Collectors.toList());
    }

    /**
     * AST递归提取文本
     */
    private static void extractText(Node node, List<String> texts) {

        if (node instanceof Text) {
            texts.add(((Text) node).getChars().toString());
        }

        for (Node child : node.getChildren()) {
            extractText(child, texts);
        }
    }

    /**
     * 文本清洗
     */
    private static String cleanText(String text) {

        return text
                // 去多余换行
                .replaceAll("\\s+", " ")
                // 去 markdown 常见符号残留
                .replaceAll("[*_`>#\\-]", "")
                // 去 URL 括号残留
                .replaceAll("\\(.*?\\)", "")
                .trim();
    }
}
