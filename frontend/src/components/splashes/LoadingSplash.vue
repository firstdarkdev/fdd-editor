<template>
<div class="absolute top-0 left-0 w-full h-full bg-ct-light-primary dark:bg-ct-dark-primary flex items-center justify-center text-black dark:text-white z-50">
  <div class="text-center">
    <img src="@/assets/img/config_editor_light.svg" class="block dark:hidden w-full" alt="logo" />
    <img src="@/assets/img/config_editor.svg" class="hidden dark:block w-full" alt="logo" />
    <br /><br />
    <h4 data-text="Loading..." class="uppercase loading-animation">Loading...</h4>
    <br />
  </div>
</div>
</template>

<script setup lang="ts">
import {onMounted} from "vue";
import {useAppState} from "@/stores/appstate";

onMounted(() => {
  setTimeout(() => {
    useAppState().setSplashScreen(false);
  }, 3000);
})
</script>

<style lang="css" scoped>
.loading-animation {
  position: relative;
  font-size: 3em;
}

.loading-animation:before {
  content: attr(data-text);
  position: absolute;
  overflow: hidden;
  max-width: 7em;
  white-space: nowrap;
  animation: loading 5s linear;
}
@keyframes loading {
  0% {
    max-width: 0;
  }
}
</style>