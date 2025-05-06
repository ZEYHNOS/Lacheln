import React, { useEffect, useState, useImperativeHandle, forwardRef } from 'react';
import { useEditor, EditorContent } from '@tiptap/react';
import StarterKit from '@tiptap/starter-kit';
import Link from '@tiptap/extension-link';
import Underline from '@tiptap/extension-underline';
import TextStyle from '@tiptap/extension-text-style';
import Color from '@tiptap/extension-color';
import TextAlign from '@tiptap/extension-text-align';
import Placeholder from '@tiptap/extension-placeholder';
import { SketchPicker } from 'react-color';
import { FontSize } from './FontSize';

import BoldIcon from '../../../image/AddWrite/bold.png';
import TextAlignLeft from '../../../image/AddWrite/textalignleft.png';
import TextAlignCenter from '../../../image/AddWrite/textaligncenter.png';
import TextAlignRight from '../../../image/AddWrite/textalignright.png';
import Palette from '../../../image/AddWrite/palette.png';
import AddYoutube from '../../../image/AddWrite/addyoutube.png';
import AddLink from '../../../image/AddWrite/addlink.png';
import AddImage from '../../../image/AddWrite/addimage.png';

import { Node, mergeAttributes } from '@tiptap/core';
import Youtube from '@tiptap/extension-youtube';

const InlineImage = Node.create({
  name: 'inlineImage',
  inline: true,
  group: 'inline',
  selectable: true,
  draggable: true,
  atom: true,
  addAttributes() {
    return {
      src: { default: null },
      alt: { default: null },
      title: { default: null },
      class: {
        default: 'inline-image max-w-[300px] align-middle',
        parseHTML: element => element.getAttribute('class'),
        renderHTML: attributes => ({ class: attributes.class }),
      },
    };
  },
  parseHTML() {
    return [{ tag: 'img[src]' }];
  },
  renderHTML({ HTMLAttributes }) {
    return ['img', mergeAttributes(HTMLAttributes)];
  },
});

const CustomYoutube = Youtube.extend({
  renderHTML({ HTMLAttributes }) {
    return [
      'div',
      { class: 'youtube-wrapper' },
      [
        'iframe',
        mergeAttributes(
          {
            width: '50%',
            height: '50%',
            frameBorder: '0',
            allowFullScreen: true,
            allow: 'accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture',
          },
          HTMLAttributes
        )
      ]
    ];
  }
});

const AddWrite = forwardRef(({ onImageUpload }, ref) => {
  const [showColorPicker, setShowColorPicker] = useState(false);

  const editor = useEditor({
    extensions: [
      StarterKit.configure(),
      Underline,
      TextStyle,
      Color,
      FontSize.configure({ types: ['textStyle'], marks: [] }),
      TextAlign.configure({ types: ['heading', 'paragraph'] }),
      InlineImage,
      Link.configure({ openOnClick: true }),
      CustomYoutube.configure({ width: 640, height: 360 }),
      Placeholder.configure({ placeholder: ' 내용을 여기에 작성하세요 ' }),
    ],
    content: '',
  });

  useImperativeHandle(ref, () => ({
    getContentAsJsonArray: () => {
      if (!editor) return [];
    
      const result = [];
    
      const traverse = (nodes) => {
        if (!nodes) return;
    
        nodes.forEach((node) => {
          if (node.type === 'paragraph' && node.content) {
            node.content.forEach((child) => {
              if (child.type === 'text') {
                const text = child.text?.trim();
                if (text) {
                  result.push({ type: 'TEXT', value: text });
                }
              } else if (child.type === 'inlineImage') {
                result.push({ type: 'IMAGE', value: child.attrs?.src });
              } else if (child.type === 'youtube') {
                result.push({ type: 'YOUTUBE', value: child.attrs.src });
              }
            });
          } else if (node.type === 'inlineImage') {
            result.push({ type: 'IMAGE', value: node.attrs?.src });
          } else if (node.type === 'youtube') {
            result.push({ type: 'YOUTUBE', value: node.attrs.src });
          } else if (node.content) {
            traverse(node.content);
          }
        });
      };
    
      const json = editor.getJSON();
      traverse(json.content);
    
      return result.map((item, index) => ({ ...item, order: index }));
    },
  
    setHTML: (html) => editor?.commands.setContent(html),
    getHTML: () => editor?.getHTML() || ''
  }));

  useEffect(() => {
    if (!editor) return;
    const handlePaste = (event) => {
      const pastedText = event.clipboardData.getData('text');
      if (/^(http|https):\/\/[\S]+$/.test(pastedText)) {
        event.preventDefault();
        editor.chain().focus().insertContent(`<a href="${pastedText}" target="_blank">${pastedText}</a>`).run();
      }
    };
    const handleDrop = (event) => event.preventDefault();
    const editorEl = document.querySelector('.ProseMirror');
    editorEl?.addEventListener('paste', handlePaste);
    editorEl?.addEventListener('drop', handleDrop);
    return () => {
      editorEl?.removeEventListener('paste', handlePaste);
      editorEl?.removeEventListener('drop', handleDrop);
    };
  }, [editor]);

  useEffect(() => {
    const preventDefault = (e) => e.preventDefault();
    window.addEventListener('dragover', preventDefault);
    window.addEventListener('drop', preventDefault);
    return () => {
      window.removeEventListener('dragover', preventDefault);
      window.removeEventListener('drop', preventDefault);
    };
  }, []);

  if (!editor) return null;

  const insertImage = () => {
    const fileInput = document.createElement('input');
    fileInput.type = 'file';
    fileInput.accept = 'image/*';
  
    fileInput.onchange = () => {
      const file = fileInput.files[0];
      if (!file) return;
  
      const reader = new FileReader();
      reader.onload = () => {
        const base64 = reader.result;
        editor.chain().focus().insertContent({
          type: 'inlineImage',
          attrs: {
            src: base64,
            class: 'inline-image max-w-[300px] align-middle',
          },
        }).run();
      };
      reader.readAsDataURL(file);
    };
  
    fileInput.click();
  };
  

  const insertYoutube = () => {
    let url = prompt('YouTube URL을 입력하세요:');
    if (!url) return;
    const match = url.match(/(?:youtu\.be\/|youtube\.com\/watch\?v=)([\w-]+)/);
    if (match && match[1]) {
      url = `https://www.youtube.com/embed/${match[1]}`;
    }
    editor.chain().focus().setYoutubeVideo({ src: url }).run();
  };

  const insertLink = () => {
    const url = prompt('링크 URL 입력:');
    if (!url) return;
    const { empty } = editor.state.selection;
    if (empty) {
      editor.chain().focus().insertContent(`<a href="${url}" target="_blank">${url}</a>`).run();
    } else {
      editor.chain().focus().extendMarkRange('link').setLink({ href: url }).run();
    }
  };

  const alignImageOrText = (alignment) => {
    const { state, view } = editor;
    const { selection } = state;
    const pos = selection.$anchor.pos;
    const nodeAt = view.state.doc.nodeAt(pos);
    const alignmentClass =
      alignment === 'left' ? 'float-left max-w-[300px] w-full mr-4' :
      alignment === 'right' ? 'float-right max-w-[300px] w-full ml-4' :
      'mx-auto block max-w-[300px]';
    if (nodeAt && (nodeAt.type.name === 'inlineImage' || nodeAt.type.name === 'youtube')) {
      try {
        const tr = editor.state.tr.setNodeMarkup(pos, nodeAt.type, {
          ...nodeAt.attrs,
          class: alignmentClass,
        });
        view.dispatch(tr);
      } catch (err) {
        console.error('정렬 중 오류 발생:', err);
      }
    } else {
      editor.chain().focus().setNode('paragraph').setTextAlign(alignment).run();
    }
  };

  return (
    <div className="p-4 bg-white shadow rounded-xl">
      <style>{`.youtube-wrapper { position: relative; padding-bottom: 56.25%; height: 0; overflow: hidden; max-width: 100%; }
              .youtube-wrapper iframe { position: absolute; top: 0; left: 0; width: 75%; height: 75%; }`}</style>
      <div className="flex flex-wrap items-center gap-2 mb-4 bg-gray-100 p-2 rounded-md">
        <button onClick={() => editor.chain().focus().toggleBold().run()} className={`btn-toolbar ${editor.isActive('bold') ? 'bg-gray-300' : ''}`}>
          <img src={BoldIcon} alt="글씨 두껍게" className="w-5 h-5" />
        </button>
        <button onClick={() => editor.chain().focus().toggleItalic().run()} className="btn-toolbar italic font-bold">/</button>
        <button onClick={() => editor.chain().focus().toggleUnderline().run()} className="btn-toolbar underline">U</button>
        <button onClick={() => editor.chain().focus().toggleStrike().run()} className="btn-toolbar line-through">S</button>
        <select onChange={(e) => editor.chain().focus().setFontSize(e.target.value).run()} className="btn-toolbar bg-white text-black">
          <option value="32px">제목1</option>
          <option value="24px">제목2</option>
          <option value="20px">제목3</option>
          <option value="16px">본문1</option>
          <option value="14px">본문2</option>
          <option value="12px">본문3</option>
        </select>
        <button onClick={() => setShowColorPicker(!showColorPicker)} className="btn-toolbar flex items-center gap-1">
          <img src={Palette} alt="색상 선택하기" className="w-5 h-5" /> 색상
        </button>
        {showColorPicker && (
          <div className="absolute z-10 mt-2">
            <SketchPicker
              color="#000"
              onChangeComplete={(color) => {
                editor.chain().focus().setColor(color.hex).run();
                setShowColorPicker(false);
              }}
            />
          </div>
        )}
        <button onClick={() => alignImageOrText('left')} className="btn-toolbar">
          <img src={TextAlignLeft} alt="좌측 정렬" className="w-5 h-5" />
        </button>
        <button onClick={() => alignImageOrText('center')} className="btn-toolbar">
          <img src={TextAlignCenter} alt="중앙 정렬" className="w-5 h-5" />
        </button>
        <button onClick={() => alignImageOrText('right')} className="btn-toolbar">
          <img src={TextAlignRight} alt="우측 정렬" className="w-5 h-5" />
        </button>
        <button onClick={insertYoutube} className="btn-toolbar flex items-center gap-1">
          <img src={AddYoutube} alt="유튜브 추가하기" className="w-5 h-5" /> 유튜브
        </button>
        <button onClick={insertLink} className="btn-toolbar flex items-center gap-1">
          <img src={AddLink} alt="링크 추가하기" className="w-5 h-5" /> 링크
        </button>
        <button onClick={insertImage} className="btn-toolbar flex items-center gap-1">
          <img src={AddImage} alt="이미지 추가하기" className="w-5 h-5" /> 이미지
        </button>
      </div>
      <EditorContent
        editor={editor}
        className="min-h-[400px] border-[2px] border-[#845ec2] p-4 rounded bg-white text-black outline-none focus:outline-none overflow-hidden"
      />
    </div>
  );
});

export default AddWrite;
