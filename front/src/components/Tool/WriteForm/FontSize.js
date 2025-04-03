import { Mark, mergeAttributes } from '@tiptap/core';

export const FontSize = Mark.create({
    name: 'fontSize',

    addOptions() {
        return {
        HTMLAttributes: {},
        };
    },

    parseHTML() {
        return [
        {
            style: 'font-size',
        },
        ];
    },

    renderHTML({ HTMLAttributes }) {
        return ['span', mergeAttributes(this.options.HTMLAttributes, HTMLAttributes), 0];
    },

    addAttributes() {
        return {
        fontSize: {
            default: null,
            parseHTML: element => element.style.fontSize,
            renderHTML: attributes => {
            if (!attributes.fontSize) return {};
            return {
                style: `font-size: ${attributes.fontSize}`,
            };
            },
        },
        };
    },

    addCommands() {
        return {
        setFontSize:
            size =>
            ({ commands }) => {
            return commands.setMark(this.name, { fontSize: size });
            },
        };
    },
});
