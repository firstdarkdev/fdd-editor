<template>
  <div class="w-full editorbar bg-ct-card-light dark:bg-ct-card-dark">
    <div class="flex items-center">
      <img src="@/assets/img/config_editor.svg" class="hidden dark:block" style="max-width: 300px;" alt="logo" />
      <img src="@/assets/img/config_editor_light.svg" class="block dark:hidden" style="max-width: 300px;" alt="logo" />
      <span class="ml-2 bg-green-100 text-green-800 text-xs font-medium me-2 px-2.5 py-0.5 rounded dark:bg-gray-700 dark:text-green-400 border border-green-400">Alpha</span>
    </div>

    <div class="button-bar flex gap-1">

      <a href="https://github.com/hypherionmc/fdd-editor" target="_blank" class="editor-button" title="Report Issue">
        <FontAwesomeIcon :icon="faGithub" />
      </a>

      <a @click="saveConfigFile(true)" href="javascript:void(0);" class="editor-button" title="Download Config">
        <FontAwesomeIcon :icon="faSave" />
      </a>

      <a @click="saveConfigFile()" href="javascript:void(0);" class="editor-button" title="View Config">
        <FontAwesomeIcon :icon="faCode" />
      </a>

      <a href="javascript:window.location.href = '/';" class="editor-button" title="Close Config">
        <FontAwesomeIcon :icon="faPowerOff" />
      </a>
    </div>
  </div>
</template>

<script setup lang="ts">
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import { faCode, faEye, faFileAlt, faPowerOff, faQuestion, faSave, faTimes } from '@fortawesome/free-solid-svg-icons'
import { saveConfigFile } from '@/composables/EditorFunctions'
import { faGithub } from '@fortawesome/free-brands-svg-icons'
</script>