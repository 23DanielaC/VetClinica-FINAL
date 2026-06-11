<template>
  <div class="flex items-center justify-end gap-1">
    <button
      type="button"
      title="Ver cliente"
      class="p-2 rounded-xl text-slate-500 hover:text-indigo-600 hover:bg-indigo-50 transition-colors"
      @click.stop="$emit('view', cliente)"
    >
      <EyeIcon class="w-4 h-4" />
    </button>
    <button
      type="button"
      title="Editar"
      class="p-2 rounded-xl text-slate-500 hover:text-blue-600 hover:bg-blue-50 transition-colors"
      @click.stop="$emit('edit', cliente)"
    >
      <PencilSquareIcon class="w-4 h-4" />
    </button>
    <button
      type="button"
      title="Historial"
      class="p-2 rounded-xl text-slate-500 hover:text-violet-600 hover:bg-violet-50 transition-colors hidden sm:inline-flex"
      @click.stop="$emit('history', cliente)"
    >
      <ClipboardDocumentListIcon class="w-4 h-4" />
    </button>
    <button
      type="button"
      title="Eliminar"
      class="p-2 rounded-xl text-slate-500 hover:text-rose-600 hover:bg-rose-50 transition-colors"
      @click.stop="$emit('delete', cliente)"
    >
      <TrashIcon class="w-4 h-4" />
    </button>
  </div>
</template>

<script setup>
import { EyeIcon, PencilSquareIcon, TrashIcon, ClipboardDocumentListIcon } from '@heroicons/vue/24/outline'

defineProps({ cliente: { type: Object, required: true } })
defineEmits(['view', 'edit', 'history', 'delete'])
</script>
