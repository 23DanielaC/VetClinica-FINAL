<template>
  <div class="card p-0 overflow-hidden">
    <!-- Header -->
    <div class="flex flex-col sm:flex-row sm:items-center justify-between gap-3 p-4 border-b border-gray-100">
      <div class="relative">
        <MagnifyingGlassIcon class="w-4 h-4 absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" />
        <input v-model="search" type="text" :placeholder="`Buscar ${title}...`"
          class="pl-9 pr-4 py-2 text-sm border border-gray-200 rounded-lg bg-base focus:outline-none focus:ring-2 focus:ring-primary w-64" />
      </div>
      <button @click="$emit('add')" class="btn-primary text-sm">
        <PlusIcon class="w-4 h-4" />
        Nuevo
      </button>
    </div>

    <!-- Table -->
    <div class="overflow-x-auto">
      <table class="w-full">
        <thead class="bg-base border-b border-gray-100">
          <tr>
            <th v-for="col in columns" :key="col.key" class="table-th">{{ col.label }}</th>
            <th class="table-th text-right">Acciones</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-gray-50">
          <tr v-for="row in filtered" :key="row.id" class="hover:bg-blue-50/30 transition-colors">
            <td v-for="col in columns" :key="col.key" class="table-td">
              <slot :name="`cell-${col.key}`" :row="row">
                {{ row[col.key] }}
              </slot>
            </td>
            <td class="table-td text-right">
              <div class="flex items-center justify-end gap-2">
                <button @click="$emit('edit', row)" class="p-1.5 rounded-lg hover:bg-blue-100 text-blue-500 transition-colors" title="Editar">
                  <PencilSquareIcon class="w-4 h-4" />
                </button>
                <button @click="$emit('delete', row)" class="p-1.5 rounded-lg hover:bg-red-100 text-red-500 transition-colors" title="Eliminar">
                  <TrashIcon class="w-4 h-4" />
                </button>
              </div>
            </td>
          </tr>
          <tr v-if="filtered.length === 0">
            <td :colspan="columns.length + 1" class="text-center py-10 text-gray-400 text-sm">
              No se encontraron registros
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Footer count -->
    <div class="px-4 py-3 border-t border-gray-100 text-xs text-gray-400">
      {{ filtered.length }} de {{ rows.length }} registros
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { MagnifyingGlassIcon, PlusIcon, PencilSquareIcon, TrashIcon } from '@heroicons/vue/24/outline'

const props = defineProps({
  title: String,
  columns: Array,
  rows: Array,
  searchKeys: { type: Array, default: () => [] }
})
defineEmits(['add', 'edit', 'delete'])

const search = ref('')
const filtered = computed(() => {
  if (!search.value) return props.rows
  const q = search.value.toLowerCase()
  return props.rows.filter(r =>
    props.searchKeys.some(k => String(r[k] ?? '').toLowerCase().includes(q))
  )
})
</script>
