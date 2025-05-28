// Pagination.jsx
import React from "react";

export default function Pagination({ current, total, onChange }) {
  const pageNumbers = [];
  for (let i = 1; i <= total; i++) {
    pageNumbers.push(i);
  }

  return (
    <div className="flex justify-center mt-6 space-x-2">
      <button
        disabled={current === 1}
        onClick={() => onChange(1)}
        className="px-3 py-1 border rounded disabled:text-gray-400 disabled:border-gray-300"
      >
        처음
      </button>

      <button
        disabled={current === 1}
        onClick={() => onChange(current - 1)}
        className="px-3 py-1 border rounded disabled:text-gray-400 disabled:border-gray-300"
      >
        이전
      </button>

      {pageNumbers.map((num) => (
        <button
          key={num}
          onClick={() => onChange(num)}
          className={`px-3 py-1 border rounded ${
            num === current
              ? "bg-[#845EC2] text-white border-[#845EC2]"
              : "hover:bg-purple-100 text-gray-700"
          }`}
        >
          {num}
        </button>
      ))}

      <button
        disabled={current === total}
        onClick={() => onChange(current + 1)}
        className="px-3 py-1 border rounded disabled:text-gray-400 disabled:border-gray-300"
      >
        다음
      </button>

      <button
        disabled={current === total}
        onClick={() => onChange(total)}
        className="px-3 py-1 border rounded disabled:text-gray-400 disabled:border-gray-300"
      >
        끝
      </button>
    </div>
  );
}