const colors = [
  'bg-cyan-900/50 text-cyan-300 border-cyan-700',
  'bg-purple-900/50 text-purple-300 border-purple-700',
  'bg-emerald-900/50 text-emerald-300 border-emerald-700',
  'bg-rose-900/50 text-rose-300 border-rose-700',
  'bg-amber-900/50 text-amber-300 border-amber-700',
  'bg-blue-900/50 text-blue-300 border-blue-700',
  'bg-fuchsia-900/50 text-fuchsia-300 border-fuchsia-700',
];

export const getTagColor = (tagName) => {
  let hash = 0;
  for (let i = 0; i < tagName.length; i++) {
    hash = tagName.charCodeAt(i) + ((hash << 5) - hash);
  }
  const index = Math.abs(hash) % colors.length;
  return colors[index];
};