<template>
  <div class="z-10 popup flex items-center justify-center" v-if="useEditor().hasSavedConfig">
    <div class="popup_dialog bg-ct-light-primary dark:bg-ct-dark-primary text-center p-4">
      <FontAwesomeIcon :icon="faClose" class="cls_btn" @click="useEditor().setTomlConfig(undefined)" />

      <h1 class="font-bold text-xl">Your new config</h1>
      <p class="text-sm mb-4">Here is your new config. Ready to copy and paste</p>

      <highlightjs
        language="toml"
        :code="useEditor().tomlConfig"
      />
      <br />
      <button type="button" @click="copyToClipboard" class="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 me-2 mb-2 dark:bg-blue-600 dark:hover:bg-blue-700 focus:outline-none dark:focus:ring-blue-800">Copy to Clipboard</button>
    </div>
  </div>
</template>
<script setup lang="ts">
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import { useEditor } from '@/stores/editor.js'
import { faClose } from '@fortawesome/free-solid-svg-icons'
import { useToast } from '@/stores/toaststore'

const copyToClipboard = async () => {
  await navigator.clipboard.writeText(useEditor().getTomlConfig)
  useToast().showToast('Copied to Clipboard', 3000, 'info');
}
</script>