<template>
  <div class="content-container" style="height: 100vh !important;" v-if="useEditor().isConfigLoaded" @mousemove="updateTooltipPosition">
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

    <div class="mctooltip text-white" :style="tooltipStyle" v-if="useAppState().shouldShowTooltip">
      <div class="mctooltip-inner">
        <p class="title">{{ useAppState().getToolTipDate.title }}</p>
        <p class="downloads">{{ useAppState().getToolTipDate.body }}</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {useEditor} from "@/stores/editor";
import EditorField from "@/components/editor/EditorField.vue";
import {FontAwesomeIcon} from "@fortawesome/vue-fontawesome";
import { faPlus, faTrashAlt } from '@fortawesome/free-solid-svg-icons'
import { addToArray, headerToDisplay } from '@/composables/FieldUtils'
import { initFlowbite } from 'flowbite'
import { computed, ref, watch } from 'vue'
import { useAppState } from '@/stores/appstate'
import { BACKEND_URL } from '@/composables/EditorFunctions'
import { useToast } from '@/stores/toaststore'

const props = defineProps(['id'])

watch(() => useEditor().getCurrentSection, () => {
  initFlowbite()
})

const deleteArrayEntry = (target: any, index: any) => {
  target.splice(index, 1);
}

const tooltipPosition = ref({ x: 0, y: 0 });

const updateTooltipPosition = (e: MouseEvent) => {
  tooltipPosition.value = {
    x: e.pageX - window.scrollX,
    y: e.pageY
  };
};

const tooltipStyle = computed(() => {
  let left = tooltipPosition.value.x;
  let top = tooltipPosition.value.y + 10;

  return {
    left: `${left}px`,
    top: `${top}px`
  };
});

if (props.id) {
  const ws = new WebSocket(`${BACKEND_URL}/ws/frontend?identifier=${props.id}`);
  useAppState().setIdentifier(props.id);

  ws.onopen = () => {
    ws.send(JSON.stringify({
      socketCode: "WS_CHECK_SESSION",
      identifier: props.id
    }));

    setInterval(() => ws.send("ping"), 20000);
  }

  ws.onmessage = (event) => {
    const data = JSON.parse(event.data);

    if (data.socketCode === 'WS_VALID_SESSION') {
      useToast().showToast('Session opened. Waiting for data', 2000, "success");
      ws.send(JSON.stringify({
        socketCode: "WS_GET_CONFIG",
        identifier: props.id
      }));
    }

    if (data.socketCode === 'WS_INVALID_SESSION') {
      useToast().showToast('Invalid Session. Please try again', 2000, "error");
    }

    if (data.socketCode === 'WS_CONFIG_ERROR') {
      useToast().showToast(data.message, 2000, 'error');
    }

    if (data.socketCode === 'WS_SEND_CONFIG') {
      const dt = JSON.parse(data.message);
      useAppState().setSplashScreen(false);
      useEditor().setConfig(dt);
      useEditor().setSocketConfig(true);
      useEditor().setConfigLoaded(true);
      useEditor().setCurrentSection(Object.keys(dt.config)[0]);
      useToast().showToast('Success', 2000, "success");
    }
  }

  ws.onclose = (event) => {
    useEditor().setConfigLoaded(false);
    useToast().showToast('Web Socket Connection Terminated', 2000, "error");
    setTimeout(() => {
      window.location.href = "/";
    }, 2000);
  }

}
</script>

<style>
.mctooltip {
  min-width: 50px;
  max-width: 300px;
  min-height: 50px;
  background: rgba(0, 0, 0, 0.8);
  position: absolute;
  top: 0;
  left: 0;
  border-radius: 5px;
  padding: 2px;
  z-index: 5000;
}

.mctooltip-inner {
  border: 2px solid #290560;
  padding: 2px 5px;
}

.mctooltip .title {
  margin-bottom: 0;
  font-family: 'MC', Fallback, sans-serif !important;
  color: yellow;
  font-size: 18px;
}

.mctooltip .downloads {
  font-family: 'MC', Fallback, sans-serif !important;
  font-size: 14px;
  color: aqua;
}
</style>