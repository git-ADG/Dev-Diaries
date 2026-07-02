import React, { useContext } from 'react';
import { useQuery } from '@tanstack/react-query';
import api from '../services/api';
import { AuthContext } from '../context/authContext';
import { LogOut, Plus, Search, FileCode2 } from 'lucide-react';
import { getTagColor } from '../utils/tagColors';
import { format } from 'date-fns';
import NoteEditor from '../components/noteEditor';

const Dashboard = () => {
  const { logout } = useContext(AuthContext);

  const [isEditorOpen, setIsEditorOpen] = useState(false);
  const [selectedNote, setSelectedNote] = useState(null);

  const handleSaveNote = async (data) => {
    if (selectedNote) {
      await api.put(`/notes/${selectedNote.id}`, data);
    } else {
      await api.post('/notes/', data);
    }
    setIsEditorOpen(false);
  };

  const { data: notes, isLoading } = useQuery({
    queryKey: ['notes'],
    queryFn: async () => {
      const response = await api.get('/notes/');
      return response.data.content || response.data; 
    }
  });

  return (
    <div className="min-h-screen bg-gray-950 p-6">
      {/* Navbar */}
      <header className="max-w-7xl mx-auto flex items-center justify-between mb-8 pb-6 border-b border-gray-800">
        <div className="flex items-center gap-2 text-cyan-400">
          <FileCode2 size={28} />
          <h1 className="text-2xl font-bold tracking-tight">Dev Diaries</h1>
        </div>
        <div className="flex items-center gap-4">
          <button className="p-2 text-gray-400 hover:text-white transition-colors bg-gray-900 rounded-lg border border-gray-800">
            <Search size={20} />
          </button>
          <button className="flex items-center gap-2 bg-cyan-600 hover:bg-cyan-500 text-white px-4 py-2 rounded-lg transition-colors font-medium">
            <Plus size={20} /> New Entry
          </button>
          <button onClick={logout} className="p-2 text-gray-400 hover:text-red-400 transition-colors">
            <LogOut size={20} />
          </button>
        </div>
      </header>

      {/* Grid */}
      <main className="max-w-7xl mx-auto">
        {isLoading ? (
          <div className="text-gray-500 text-center mt-20">Loading knowledge base...</div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {notes?.map((note) => (
              <div key={note.id} className="bg-gray-900 border border-gray-800 rounded-xl p-5 hover:border-cyan-500/50 hover:shadow-[0_0_15px_rgba(6,182,212,0.15)] transition-all cursor-pointer group">
                <div className="flex justify-between items-start mb-3">
                  <span className="text-xs font-mono text-gray-500">{format(new Date(note.createdAt), 'MMM dd, yyyy')}</span>
                  <span className="text-xs font-mono px-2 py-1 bg-gray-950 border border-gray-800 rounded text-gray-400">{note.format}</span>
                </div>
                <h2 className="text-lg font-semibold text-gray-100 mb-4 group-hover:text-cyan-400 transition-colors line-clamp-2">
                  {note.title}
                </h2>
                
                {/* Tags */}
                <div className="flex flex-wrap gap-2 mt-auto">
                  {note.tags?.map(tag => (
                    <span key={tag} className={`text-xs px-2 py-1 rounded-md border ${getTagColor(tag)}`}>
                      #{tag}
                    </span>
                  ))}
                </div>
              </div>
            ))}
          </div>
        )}
        {isEditorOpen && (
        <NoteEditor 
          note={selectedNote} 
          onSave={handleSaveNote} 
          onClose={() => setIsEditorOpen(false)} 
        />
      )}
      </main>
    </div>
  );
};

export default Dashboard;