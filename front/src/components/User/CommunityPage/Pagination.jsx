import React from "react";

export default function Pagination({ current, total, onChange }) {
  const pageNumbers = [];
  for (let i = 1; i <= total; i++) {
    pageNumbers.push(i);
  }

  const baseBtn =
    "px-3 py-1 border rounded bg-white text-black border-gray-300 hover:bg-purple-100";

  const disabledBtn =
    "px-3 py-1 border rounded bg-white text-gray-400 border-gray-200 cursor-not-allowed";

  return (
    <div className="flex justify-center mt-6 space-x-2">
      <button
        disabled={current === 1}
        onClick={() => onChange(1)}
        className={current === 1 ? disabledBtn : baseBtn}
      >
        처음
      </button>

      <button
        disabled={current === 1}
        onClick={() => onChange(current - 1)}
        className={current === 1 ? disabledBtn : baseBtn}
      >
        이전
      </button>

      {pageNumbers.map((num) => (
        <button
          key={num}
          onClick={() => onChange(num)}
          className={
            num === current
              ? "px-3 py-1 border rounded bg-[#845EC2] text-white border-[#845EC2]"
              : baseBtn
          }
        >
          {num}
        </button>
      ))}

      <button
        disabled={current === total}
        onClick={() => onChange(current + 1)}
        className={current === total ? disabledBtn : baseBtn}
      >
        다음
      </button>

      <button
        disabled={current === total}
        onClick={() => onChange(total)}
        className={current === total ? disabledBtn : baseBtn}
      >
        끝
      </button>
    </div>
  );
}
