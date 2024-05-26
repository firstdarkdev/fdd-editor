<template>
  <div class="content-container" v-if="useEditor().isConfigLoaded">
    <div class="flex gap-4 items-center bg-ct-card-light dark:bg-ct-card-dark p-4 cursor-pointer overflow-x-auto" style="border-radius: 5px" v-if="useEditor().isConfigLoaded">
      <p class="tab-key" @click="useEditor().setCurrentSection(key)" v-for="key in Object.keys(useEditor().getConfig.config as any)" :class="useEditor().getCurrentSection === key ? 'active_key' : ''">
        {{ headerToDisplay(key) }}
      </p>
    </div>

    <div class="bg-ct-card-light dark:bg-ct-card-dark p-4 rounded-lg mt-5 relative">
      <h1 class="font-bold text-2xl">{{ headerToDisplay(useEditor().getCurrentSection) }} Config</h1>
      <p
        v-if="useEditor().getCurrentSection === 'dimension_overrides'"
        class="plus_button"
        @click="addToArray(useEditor().getConfig.config.dimension_overrides.dimensions, 'dimensions')"
      >
        <FontAwesomeIcon :icon="faPlus" />
      </p>

      <div class="editor-body mt-10" v-if="useEditor().isConfigLoaded">
        <div v-for="(value, key) in useEditor().getConfig.config[useEditor().getCurrentSection]" >
          <EditorField :key="key" :target="useEditor().getConfig.config[useEditor().currentSection]" :identifier="key" :value="value" v-if="typeof value !== 'object'" />

          <div v-if="typeof value === 'object' && key as string !== 'dimensions'" class="bg-ct-card-light dark:bg-ct-card-dark p-4 mt-2 mb-2 rounded-lg relative">
            <h1 class="font-bold mb-4 underline">{{ headerToDisplay(key) }}</h1>
            <p class="plus_button" @click="addToArray(useEditor().getConfig.config[useEditor().currentSection][key], key)" v-if="Array.isArray(useEditor().getConfig.config[useEditor().currentSection][key])"><FontAwesomeIcon :icon="faPlus" /></p>
            <EditorField v-for="(vv, kk) in value" :key="kk" :target="useEditor().getConfig.config[useEditor().currentSection][key]" :identifier="kk" :value="vv" />
          </div>
        </div>

        <!-- === Dimension Overrides Controls === -->
        <div v-if="useEditor().isConfigLoaded && useEditor().currentSection === 'dimension_overrides'">

          <div v-for="(dimension, key) in useEditor().getConfig.config.dimension_overrides.dimensions" style="margin-bottom: 10px;" class="bg-ct-card-light dark:bg-ct-card-dark p-4 mt-2 mb-2 rounded-lg relative">
            <h1 class="font-bold text-xl">
              {{ headerToDisplay(useEditor().getConfig.config.dimension_overrides.dimensions[key].name) || 'No Name' }}
            </h1>
            <p
              class="plus_button"
              @click="deleteArrayEntry(useEditor().getConfig.config.dimension_overrides.dimensions, key)"
            >
              <FontAwesomeIcon :icon="faTrashAlt" />
            </p>

            <div v-for="(subval, subkey) in useEditor().getConfig.config.dimension_overrides.dimensions[key]">
              <EditorField
                :key="subkey"
                v-if="typeof subval !== 'object'"
                :identifier="subkey"
                :value="subval"
                :target="useEditor().getConfig.config.dimension_overrides.dimensions[key]"
              />

              <div v-if="typeof subval === 'object'" class="bg-ct-card-light dark:bg-ct-card-dark p-4 mt-2 mb-2 rounded-lg relative">
                <h1 class="font-bold mb-4 underline">{{ headerToDisplay(subkey) }}</h1>
                <p
                  class="plus_button"
                  @click="addToArray(useEditor().getConfig.config.dimension_overrides.dimensions[key][subkey], subkey)"
                >
                  <FontAwesomeIcon :icon="faPlus" />
                </p>

                <EditorField
                  v-for="(vv, kk) in subval"
                  :key="kk"
                  :target="useEditor().getConfig.config.dimension_overrides.dimensions[key][subkey]"
                  :identifier="kk"
                  :value="vv"
                />
              </div>
            </div>
          </div>

        </div>
        <!-- === End Dimension Overrides Controls === -->
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {useEditor} from "@/stores/editor";
import EditorField from "@/components/editor/EditorField.vue";
import {FontAwesomeIcon} from "@fortawesome/vue-fontawesome";
import { faPlus, faTrash, faTrashAlt } from '@fortawesome/free-solid-svg-icons'
import { addToArray, headerToDisplay } from '@/composables/FieldUtils'
import { initFlowbite } from 'flowbite'
import { watch } from 'vue'

watch(() => useEditor().getCurrentSection, () => {
  initFlowbite()
})

const deleteArrayEntry = (target: any, index: any) => {
  target.splice(index, 1);
}
</script>