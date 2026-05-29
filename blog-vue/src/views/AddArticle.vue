```vue
<template>
  <div class="create-container">

    <!-- 顶部 -->
    <div class="create-header">

      <!-- 标题 -->
      <el-input
          v-model="article.title"
          class="title-input"
          placeholder="请输入文章标题..."
      />

      <!-- 操作区 -->
      <div class="header-right">

        <!-- 标签 -->
        <el-input
            v-model="article.tags"
            class="tags-input"
            placeholder="标签（用逗号分隔）"
        />

        <!-- 发布 -->
        <el-button
            type="primary"
            @click="handlePublish"
        >
          发布文章
        </el-button>

      </div>
    </div>

    <!-- 编辑器 -->
    <MdEditor
        v-model="article.content"
        class="editor"
        :toolbars="toolbars"
        @onUploadImg="onUploadImg"
    />

  </div>
</template>

<script lang="ts">
import { defineComponent, reactive } from "vue";

import { ElMessage } from "element-plus";

import { MdEditor } from "md-editor-v3";

import "md-editor-v3/lib/style.css";

import service from "../utils/https";
import urls from "../utils/urls";
import LoadEnd from "../components/LoadEnd.vue";
import LoadingCustom from "../components/Loading.vue";
import {useRoute, useRouter} from "vue-router";
import {ArticlesData, ArticlesParams} from "../types";
import {getBaseUrl, getQueryStringByName} from "../utils/utils";
export default defineComponent({
  name: "Create",

  components: {
    MdEditor,
  },

  setup() {
    const state = reactive({
      isLoadEnd: false,
      isLoading: false
    });

    const router = useRouter();
    // 文章
    const article = reactive({
      title: "",
      tags: "",
      content: "",
    });

    // 工具栏
    const toolbars = [
      "bold",
      "underline",
      "italic",
      "-",
      "title",
      "strikeThrough",
      "sub",
      "sup",
      "quote",
      "unorderedList",
      "orderedList",
      "task",
      "-",
      "codeRow",
      "code",
      "link",
      "image",
      "table",
      "-",
      "revoke",
      "next",
      "save",
      "=",
      "pageFullscreen",
      "fullscreen",
      "preview",
      "previewOnly",
      "htmlPreview",
    ];

    /**
     * 上传图片
     */
    const onUploadImg = async (
        files: File[],
        callback: (urls: string[]) => void
    ) => {

      const res: string[] = [];

      for (const file of files) {

        const formData = new FormData();

        formData.append("file", file);

        /**
         * 这里换成你的后端上传接口
         */

        const data = await service.post(
            urls.uploadImage,
            formData,
            {
              needToken:true
            }
        );


        /**
         * 后端返回：
         * {
         *   url: "图片地址"
         * }
         */
        res.push(data);
      }

      callback(res);
    };

    /**
     * 发布文章
     */
    const handlePublish = async () => {

      if (!article.title) {
        ElMessage.warning("请输入标题");
        return;
      }

      if (!article.content) {
        ElMessage.warning("请输入内容");
        return;
      }

      try {
        state.isLoading=true;
        const data = await service.post(
            urls.addArticle,
            {
              d:article
            },
            {
              needToken:true
            }
        );
        state.isLoading=false;

        router.back()

      } catch (error) {
        state.isLoading=false;
        ElMessage.error("发布失败");
      }
    };

    return {
      article,
      toolbars,
      onUploadImg,
      handlePublish,
    };
  },
});
</script>

<style scoped lang="less">
.create-container {
  width: 100%;
  height: 100vh;
  background: #f5f6f7;
}

/* 顶部 */
.create-header {
  height: 70px;
  padding: 0 30px;
  background: #fff;
  border-bottom: 1px solid #eee;

  display: flex;
  align-items: center;
  justify-content: space-between;
}

/* 标题 */
.title-input {
  width: 50%;

  :deep(.el-input__wrapper) {
    box-shadow: none;
    border: none;
    background: transparent;
  }

  :deep(.el-input__inner) {
    font-size: 28px;
    font-weight: bold;
  }
}

/* 右侧 */
.header-right {
  display: flex;
  align-items: center;
  gap: 15px;
}

/* 标签 */
.tags-input {
  width: 220px;
}

/* 编辑器 */
.editor {
  height: calc(100vh - 70px);
}
</style>
```
