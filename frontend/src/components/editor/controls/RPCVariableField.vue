<template>
  <div class="grid grid-cols-2 w-full gap-2 items-center">
    <div>
      <p class="text-sm mb-1 pl-1">Variable</p>
      <UInput
        variant="subtle"
        size="lg"
        v-model="model[varNameKey]"
        class="w-full"
      />
    </div>

    <div>
      <p class="text-sm mb-1 pl-1">Value</p>
      <div class="flex gap-1">
        <UInput
          variant="subtle"
          size="lg"
          v-model="model[varValueKey]"
          class="w-full"
        />

        <UButton
          variant="ghost"
          color="error"
          icon="i-lucide-trash"
          @click="$emit('delete')"
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

const varNameKey = computed(() => {
  if (hasProp('Name')) return 'Name'
  return 'name'
})

const varValueKey = computed(() => {
  if (hasProp('Value')) return 'Value';
  return 'value';
})
</script>
