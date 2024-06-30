<template>
  <div class="absolute top-0 left-0 w-full h-full bg-ct-light-primary dark:bg-ct-dark-primary flex items-center justify-center text-black dark:text-white">
    <div class="text-center">
      <h1 class="font-extrabold text-red-600 uppercase">WARNING: Make a backup of your config when working with this editor!</h1> <br/>
      <img src="@/assets/img/config_editor_light.svg" class="block dark:hidden w-full" alt="logo" />
      <img src="@/assets/img/config_editor.svg" class="hidden dark:block w-full" alt="logo" />
      <br /><br />
      <p class="font-bold">Please choose a config file, or drag-and-drop a file here to get started</p>
      <br />
      <button type="button" @click="open()" class="text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-blue-300 dark:focus:ring-blue-800 font-medium rounded-lg text-sm px-5 py-2.5 text-center me-2 mb-2">
        Select Config File
      </button>
      <br />
      <br />
      <p class="text-yellow-500 dark:text-yellow-300 text-sm">Supports: <i>Simple RPC (3+), Simple Discord Link (3+) and Simple RPC Stardew Edition</i> configs</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import {onMounted} from "vue";
import {useFileDialog} from "@vueuse/core";
import {useEditor} from "@/stores/editor";
import {useToast} from "@/stores/toaststore";
import { BACKEND_URL } from '@/composables/EditorFunctions'

onMounted(() => {
  document.body.addEventListener('dragover', evt => {
    evt.preventDefault();
  });

  document.body.addEventListener('drop', evt => {
    evt.preventDefault();
  });
})

const { open, onChange } = useFileDialog({
  accept: '.toml'
});

onChange((files) => {
  if (files?.item(0)) {
    const formData = new FormData();
    formData.append('file', files[0]);

    fetch(`${BACKEND_URL}/v1/parseupload`, {
      method: 'POST',
      body: formData
    }).then(async res => {
      const dt = await res.json();

      if (!dt.error) {
        useEditor().setConfig(dt.data);
        useEditor().setConfigLoaded(true);
        useEditor().setCurrentSection(Object.keys(dt.data.config)[0]);
        useToast().showToast('Success', 2000, "success");
      } else {
        useToast().showToast(dt.message, 2000, "error");
      }
    }).catch(err => {
      console.error(err)
      useToast().showToast(err, 2000, "error");
    })
  }
})

</script>