<template>
  <div class="grid grid-cols-2 w-full gap-2 items-center">
    <div>
      <p class="text-sm mb-1 pl-1">Button Title</p>
      <UInput
        variant="subtle"
        size="lg"
        v-model="model[labelKey]"
        class="w-full"
      />
    </div>

    <div>
      <p class="text-sm mb-1 pl-1">Button URL</p>
      <div class="flex gap-1">
        <UInput
          variant="subtle"
          size="lg"
          v-model="model[urlKey]"
          class="w-full"
        />

        <UButton
          variant="ghost"
          color="error"
          @click="$emit('delete')"
          icon="i-lucide-trash"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {computed} from "vue";

defineEmits(['delete'])

const model = defineModel<Record<string, any>>({ required: true })

const isObjectField = computed(() => {
  return typeof model.value === 'object' && model.value !== null
})

const hasProp = (prop: string) => {
  return isObjectField.value && Object.prototype.hasOwnProperty.call(model.value, prop)
}

const urlKey = computed(() => {
  if (hasProp('Url')) return 'Url';
  return 'url';
})

const labelKey = computed(() => {
  if (hasProp('Title')) return 'Title'
  return 'label'
})
</script>
