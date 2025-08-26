<template>
  <div style="margin-top: 24px">
    <Giscus
      id="comments"
      repo="vongdefu/vongdefu.github.io"
      repoid="R_kgDON_DwvQ"
      category="Announcements"
      categoryid="DIC_kwDON_Dwvc4CnWcs"
      mapping="pathname"
      term="Welcome to giscus!"
      reactionsenabled="1"
      emitmetadata="0"
      inputposition="top"
      loading="lazy"
      :theme="isDark ? 'dark' : 'light'"
      :key="route.path"
    ></Giscus>
  </div>
</template>

<script setup>
import Giscus from "@giscus/vue";
import { useRoute, useData } from "vitepress";

const route = useRoute();
const { isDark } = useData();
</script>
