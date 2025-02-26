<template>
  <div class="z-10 popup flex items-center justify-center" v-if="useEditor().hasDownloadConfig.isSaved">
    <div class="popup_dialog_small bg-ct-light-primary dark:bg-ct-dark-primary text-center p-4">
      <FontAwesomeIcon :icon="faClose" class="cls_btn" @click="useEditor().setDownloadConfig(false, '')" />

      <h1 class="font-bold text-xl">Your new config</h1>
      <p class="text-sm mb-4">Your config is now ready to download. You can choose between downloading your file before you made changes, or downloading the modified config file</p>

      <br />
      <div class="flex items-center justify-center gap-2">
        <button type="button" @click="downloadFile(useEditor().hasDownloadConfig.original)" class="focus:outline-none text-white bg-purple-700 hover:bg-purple-800 focus:ring-4 focus:ring-purple-300 font-medium rounded-lg text-sm px-5 py-2.5 mb-2 dark:bg-purple-600 dark:hover:bg-purple-700 dark:focus:ring-purple-900">Download Original</button>
        <button type="button" @click="downloadFile(useEditor().hasDownloadConfig.modified)" class="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 me-2 mb-2 dark:bg-blue-600 dark:hover:bg-blue-700 focus:outline-none dark:focus:ring-blue-800">Download Modified</button>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import { useEditor } from '@/stores/editor.js'
import { faClose } from '@fortawesome/free-solid-svg-icons'
import { downloadFile } from '@/composables/EditorFunctions'
</script>