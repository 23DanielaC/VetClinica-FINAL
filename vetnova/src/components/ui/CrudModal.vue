<template>
  <Teleport to="body">
    <div
      v-if="open"
      class="fixed inset-0 z-[100] flex items-center justify-center p-4 bg-black/40 backdrop-blur-sm"
      @mousedown.self="close"
    >
      <div class="bg-white rounded-2xl shadow-2xl w-full max-w-lg max-h-[90vh] flex flex-col relative z-[101]">
        <div class="flex items-center justify-between px-6 py-4 border-b border-gray-100">
          <h3 class="text-lg font-semibold text-gray-800">{{ title }}</h3>
          <button type="button" @click="close" class="p-1 rounded-lg hover:bg-base text-gray-400 hover:text-gray-600 transition-colors">
            <XMarkIcon class="w-5 h-5" />
          </button>
        </div>
        <div class="overflow-y-auto px-6 py-4 flex-1">
          <slot />
        </div>
        <div v-if="!hideFooter" class="flex justify-end gap-3 px-6 py-4 border-t border-gray-100">
          <button type="button" @click="close" class="btn-secondary">{{ cancelLabel }}</button>
          <button type="button" @click="$emit('confirm')" class="btn-primary">{{ confirmLabel }}</button>
        </div>
        <div v-else class="flex justify-end gap-3 px-6 py-4 border-t border-gray-100">
          <slot name="footer" />
        </div>
      </div>
    </div>
  </Teleport>
</template>

<script setup>
import { computed } from 'vue'
import { XMarkIcon } from '@heroicons/vue/24/outline'

const props = defineProps({
  modelValue: { type: Boolean, default: false },
  title: String,
  confirmLabel: { default: 'Guardar' },
  cancelLabel: { default: 'Cancelar' },
  hideFooter: { type: Boolean, default: false },
})
const emit = defineEmits(['update:modelValue', 'confirm'])

const open = computed({
  get: () => props.modelValue,
  set: (v) => emit('update:modelValue', v),
})

function close() { open.value = false }
</script>
