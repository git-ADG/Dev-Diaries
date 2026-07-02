import React, { useState } from 'react';
import Editor from '@monaco-editor/react';
import ReactMarkdown from 'react-markdown';
import { Save, X, Code2, Trash2 } from 'lucide-react';

const SUPPORTED_LANGUAGES = [
  // Web Development
  { id: 'javascript', name: 'JavaScript' },
  { id: 'typescript', name: 'TypeScript' },
  { id: 'html', name: 'HTML' },
  { id: 'css', name: 'CSS' },
  { id: 'scss', name: 'SCSS' },
  { id: 'less', name: 'LESS' },
  { id: 'php', name: 'PHP' },

  // Systems & Application Programming
  { id: 'python', name: 'Python' },
  { id: 'java', name: 'Java' },
  { id: 'cpp', name: 'C++' },
  { id: 'c', name: 'C' },
  { id: 'csharp', name: 'C#' },
  { id: 'go', name: 'Go' },
  { id: 'rust', name: 'Rust' },
  { id: 'swift', name: 'Swift' },
  { id: 'kotlin', name: 'Kotlin' },

  // Data, Query & Configuration
  { id: 'sql', name: 'SQL' },
  { id: 'json', name: 'JSON' },
  { id: 'yaml', name: 'YAML' },
  { id: 'xml', name: 'XML' },
  { id: 'toml', name: 'TOML' },

  // Scripting & Others
  { id: 'ruby', name: 'Ruby' },
  { id: 'shell', name: 'Shell (Bash)' },
  { id: 'powershell', name: 'PowerShell' },
  { id: 'r', name: 'R' },
  { id: 'perl', name: 'Perl' },
  { id: 'lua', name: 'Lua' },
  { id: 'dart', name: 'Dart' },
  { id: 'elixir', name: 'Elixir' },
  { id: 'clojure', name: 'Clojure' }
];

const NoteEditor = ({ note, onSave, onClose, onDelete }) => {
  const [formData, setFormData] = useState({
    title: note?.title || '',
    content: note?.content || '',
    format: note?.format || 'MARKDOWN',
    language: note?.language || 'javascript'
  });

  const handleSave = () => onSave(formData);

  const getEditorLanguage = () => {
    if (formData.format === 'MARKDOWN') return 'markdown';
    if (formData.format === 'PLAIN_TEXT') return 'plaintext';
    return formData.language; 
  };

  return (
    <div className="fixed inset-0 bg-black/80 z-50 flex items-center justify-center p-4 backdrop-blur-sm">
      <div className="bg-gray-900 w-full max-w-5xl h-[85vh] flex flex-col rounded-xl border border-gray-700 shadow-2xl overflow-hidden">
        
        {/* Toolbar */}
        <div className="p-4 border-b border-gray-800 flex flex-wrap gap-4 justify-between items-center bg-gray-950">
          <input 
            value={formData.title} 
            onChange={(e) => setFormData({...formData, title: e.target.value})}
            className="bg-transparent text-xl font-bold text-gray-100 outline-none flex-1 min-w-[200px]"
            placeholder="Note Title..."
          />
          
          <div className="flex items-center gap-3">
            {/* Format Selector */}
            <select 
              value={formData.format}
              onChange={(e) => setFormData({...formData, format: e.target.value})}
              className="bg-gray-800 border border-gray-700 text-gray-200 text-sm rounded-lg px-3 py-2 outline-none focus:border-cyan-500"
            >
              <option value="MARKDOWN">Markdown</option>
              <option value="CODE">Code Snippet</option>
              <option value="PLAIN_TEXT">Plain Text</option>
            </select>

            {/* Language Selector (ONLY SHOWS IF 'CODE' IS SELECTED) */}
            {formData.format === 'CODE' && (
              <div className="flex items-center gap-2 bg-gray-800 border border-gray-700 rounded-lg px-4 py-2">
                <Code2 size={16} className="text-cyan-400" />
                <select
                  value={formData.language}
                  onChange={(e) => setFormData({...formData, language: e.target.value})}
                  className="bg-gray-800 border border-gray-700 text-gray-200 text-sm rounded-lg outline-none focus:border-cyan-500"
                >
                  {SUPPORTED_LANGUAGES.map(lang => (
                    <option key={lang.id} value={lang.id}>{lang.name}</option>
                  ))}
                </select>
              </div>
            )}

            {note?.id && (
              <button 
                onClick={() => onDelete(note.id)} 
                className="p-2 text-gray-400 hover:text-white hover:bg-red-600 transition-colors rounded-lg border border-gray-700 bg-gray-800"
                title="Delete Note"
              >
                <Trash2 size={16}/>
              </button>
            )}

            <button onClick={onClose} className="p-2 text-gray-400 hover:text-white transition-colors"><X size={20}/></button>
            <button onClick={handleSave} className="bg-cyan-600 hover:bg-cyan-500 px-4 py-2 rounded-lg text-white flex items-center gap-2 transition-colors font-medium">
              <Save size={16} /> Save
            </button>
          </div>
        </div>

        {/* Editor Area */}
        <div className="flex-1 overflow-hidden flex bg-[#1e1e1e]">
          <Editor
            height="100%"
            theme="vs-dark"
            language={getEditorLanguage()}
            value={formData.content}
            onChange={(value) => setFormData({...formData, content: value})}
            options={{
              minimap: { enabled: false },
              fontSize: 15,
              wordWrap: "on",
              padding: { top: 16 }
            }}
          />
        </div>
      </div>
    </div>
  );
};

export default NoteEditor;