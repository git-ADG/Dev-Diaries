import React, { useContext, useState } from 'react';
import { useQuery, useQueryClient } from '@tanstack/react-query';
import api from '../services/api';
import { AuthContext } from '../context/authContext';
import { LogOut, Plus, Search, FileCode2, X } from 'lucide-react';
import { getTagColor } from '../utils/tagColors';
import { format } from 'date-fns';
import NoteEditor from '../components/noteEditor';

const Dashboard = () => {
  const { logout } = useContext(AuthContext);
  const queryClient = useQueryClient();

  const [isEditorOpen, setIsEditorOpen] = useState(false);
  const [selectedNote, setSelectedNote] = useState(null);

  const [isSearchOpen, setIsSearchOpen] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');

  const [selectedTag, setSelectedTag] = useState(null);

  const { data: notes, isLoading } = useQuery({
    queryKey: ['notes', searchQuery, selectedTag], 
    queryFn: async () => {
      let url = '/notes/?';
      if (searchQuery) url += `keyword=${encodeURIComponent(searchQuery)}&`;
      if (selectedTag) url += `tagName=${encodeURIComponent(selectedTag)}&`;
      
      const response = await api.get(url); 
      return response.data.content || response.data; 
    }
  });

  const handleSaveNote = async (data) => {
    try {
      if (selectedNote) {
        await api.put(`/notes/${selectedNote.id}`, data);
      } else {
        await api.post('/notes/', data);
      }
      setIsEditorOpen(false);
      queryClient.invalidateQueries({ queryKey: ['notes'] });
    } catch (error) {
      console.error("Failed to save note:", error);
      alert("Failed to save note. Check console for details.");
    }
  };

  const handleDeleteNote = async (id) => {
    if (window.confirm("Are you sure you want to delete this note? This action cannot be undone.")) {
      try {
        await api.delete(`/notes/${id}`);
        setIsEditorOpen(false); // Close the editor
        queryClient.invalidateQueries({ queryKey: ['notes'] }); // Refresh the grid
      } catch (error) {
        console.error("Failed to delete note:", error);
      }
    }
  };

  return (
    <div className="min-h-screen bg-gray-950 p-6 flex flex-col">
      {/* Navbar */}
      <header className="max-w-7xl w-full mx-auto flex items-center justify-between mb-6 pb-6 border-b border-gray-800">
        <div className="flex items-center gap-2 text-cyan-400">
          <FileCode2 size={28} />
          <h1 className="text-2xl font-bold tracking-tight hidden sm:block">Dev Diaries</h1>
        </div>
        
        <div className="flex items-center gap-3 sm:gap-4">
          <button 
            onClick={() => setIsSearchOpen(!isSearchOpen)}
            className={`p-2 transition-colors rounded-lg border ${isSearchOpen ? 'bg-cyan-900/30 border-cyan-500 text-cyan-400' : 'bg-gray-900 border-gray-800 text-gray-400 hover:text-white'}`}
          >
            <Search size={20} />
          </button>
          
          <button 
            onClick={() => { setSelectedNote(null); setIsEditorOpen(true); }}
            className="flex items-center gap-2 bg-cyan-600 hover:bg-cyan-500 text-white px-3 py-2 sm:px-4 rounded-lg transition-colors font-medium"
          >
            <Plus size={20} /> <span className="hidden sm:inline">New Entry</span>
          </button>
          
          <button onClick={logout} className="p-2 text-gray-400 hover:text-red-400 transition-colors">
            <LogOut size={20} />
          </button>
        </div>
      </header>

      {/* Search Bar */}
      {isSearchOpen && (
        <div className="max-w-7xl w-full mx-auto mb-6">
          <div className="relative">
            <Search className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-500" size={20} />
            <input
              autoFocus
              type="text"
              placeholder="Search your knowledge base by keyword..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="w-full bg-gray-900 border border-gray-800 text-gray-100 rounded-xl py-4 pl-12 pr-10 focus:outline-none focus:border-cyan-500 transition-all shadow-lg"
            />
            {searchQuery && (
              <button onClick={() => setSearchQuery('')} className="absolute right-4 top-1/2 -translate-y-1/2 text-gray-500 hover:text-gray-300">
                <X size={20} />
              </button>
            )}
          </div>
        </div>
      )}

      {/* Active Tag Filter Indicator */}
      {selectedTag && (
        <div className="max-w-7xl w-full mx-auto mb-6 flex items-center gap-3">
          <span className="text-gray-400 text-sm font-medium">Filtering by tag:</span>
          <span className={`flex items-center gap-2 px-3 py-1 rounded-lg border text-sm font-medium cursor-pointer ${getTagColor(selectedTag)}`}
                onClick={() => setSelectedTag(null)}>
            #{selectedTag} <X size={14} className="hover:text-white" />
          </span>
        </div>
      )}

      {/* Main Grid */}
      <main className="max-w-7xl w-full mx-auto flex-1">
        {isLoading ? (
          <div className="text-gray-500 text-center mt-20 flex flex-col items-center gap-4">
            <div className="w-8 h-8 border-4 border-cyan-500 border-t-transparent rounded-full animate-spin"></div>
            Loading knowledge base...
          </div>
        ) : notes?.length === 0 ? (
          <div className="text-gray-500 text-center mt-20">
            {searchQuery || selectedTag ? "No notes found matching your filters." : "Your developer diary is empty. Create a new entry!"}
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 pb-12">
            {notes?.map((note) => (
              <div 
                key={note.id} 
                onClick={() => { setSelectedNote(note); setIsEditorOpen(true); }}
                className="bg-gray-900 border border-gray-800 rounded-xl p-5 hover:border-cyan-500/50 hover:shadow-[0_0_15px_rgba(6,182,212,0.15)] transition-all cursor-pointer group flex flex-col h-48"
              >
                <div className="flex justify-between items-start mb-3">
                  <span className="text-xs font-mono text-gray-500">{format(new Date(note.createdAt), 'MMM dd, yyyy')}</span>
                  <span className="text-[10px] font-mono px-2 py-1 bg-gray-950 border border-gray-800 rounded text-gray-400 uppercase tracking-wider">{note.format}</span>
                </div>
                <h2 className="text-lg font-semibold text-gray-100 mb-2 group-hover:text-cyan-400 transition-colors line-clamp-2">
                  {note.title}
                </h2>
                
                {/* Tags */}
                <div className="flex flex-wrap gap-2 mt-auto pt-4 border-t border-gray-800/50">
                  {note.tags?.length > 0 ? (
                    note.tags.map(tag => (
                      <span 
                        key={tag} 
                        // IMPORTANT: Stop propagation so clicking the tag doesn't open the editor!
                        onClick={(e) => { e.stopPropagation(); setSelectedTag(tag); }}
                        className={`text-xs px-2 py-1 rounded-md border hover:opacity-80 transition-opacity ${getTagColor(tag)}`}
                      >
                        #{tag}
                      </span>
                    ))
                  ) : (
                    <span className="text-xs text-gray-600 italic">No tags</span>
                  )}
                </div>
              </div>
            ))}
          </div>
        )}
      </main>

      {/* Editor Modal */}
      {isEditorOpen && (
        <NoteEditor 
          note={selectedNote} 
          onSave={handleSaveNote} 
          onClose={() => setIsEditorOpen(false)} 
          onDelete={handleDeleteNote}
        />
      )}
    </div>
  );
};

export default Dashboard;