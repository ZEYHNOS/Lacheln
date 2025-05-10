// src/components/Company/Report/ReportPage.jsx
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import AsyncSelect from "react-select/async";
import { useAuth } from "../../hooks/useAuth";

const BASE_URL = import.meta.env.VITE_API_BASE_URL;

const CATEGORY_OPTIONS = [
  { value: "GENERAL",     label: "일반 컨텐츠"           },
  { value: "SPAM",        label: "스팸"                  },
  { value: "FLAGGED",     label: "선정성이 있는 컨텐츠"  },
  { value: "BAD_SERVICE", label: "업체의 서비스가 좋지 않습니다." },
  { value: "RESTRICTED",  label: "제한된 컨텐츠"         },
];

export default function ReportPage() {
  const navigate = useNavigate();
  const { currentId } = useAuth();

  const [step,       setStep]       = useState(1);
  const [targetType, setTargetType] = useState(""); // "USER" or "COMPANY"
  const [reportedId, setReportedId] = useState("");
  const [category,   setCategory]   = useState("");
  const [title,      setTitle]      = useState("");
  const [content,    setContent]    = useState("");
  const [imageUrls,  setImageUrls]  = useState([]);
  const [files,      setFiles]      = useState([]);

  const canNext1 = Boolean(reportedId);
  const canNext2 = Boolean(category && title.trim() && content.trim());

  // ─── type‐ahead loaders ───────────────────────────────────
  const loadCompanyOptions = async input => {
    if (!input) return [];
    try {
      const { data: api } = await axios.get(
        `${BASE_URL}/company/search/${encodeURIComponent(input)}`
      );
      const c = api.data;
      return [{ value: String(c.cpId), label: c.cpName }];
    } catch {
      return [];
    }
  };

  const loadUserOptions = async input => {
    if (!input) return [];
    try {
      const { data: api } = await axios.get(
        `${BASE_URL}/profile/${encodeURIComponent(input)}`
      );
      const u = api.data;
      return [{ value: u.userId, label: u.userName }];
    } catch {
      return [];
    }
  };
  // ─────────────────────────────────────────────────────────

  // immediately upload selected files
  const handleFileChange = async e => {
    const chosen = Array.from(e.target.files || []);
    setFiles(chosen);

    const uploadedUrls = [];
    for (let file of chosen) {
      const form = new FormData();
      form.append("file", file);
      try {
        const res = await axios.post(
          `${BASE_URL}/upload`,
          form,
          { headers: { "Content-Type": "multipart/form-data" } }
        );
        uploadedUrls.push(res.data.url);
      } catch (err) {
        console.error("upload failed:", err);
        alert(`${file.name} 업로드에 실패했습니다.`);
      }
    }
    setImageUrls(uploadedUrls);
  };

  const handleSubmit = async () => {
    const payload = {
      reportTitle:    title,
      reportContent:  content,
      reportCategory: category,
      reportTarget:   targetType,
      cpId:    targetType === "COMPANY" ? Number(reportedId) : null,
      userId:  targetType === "USER"    ? reportedId : null,
      imageUrls: imageUrls.map(url => ({ reportImageUrl: url })),
    };

    const endpoint =
      targetType === "USER"
        ? `${BASE_URL}/report/company/${currentId}`
        : `${BASE_URL}/report/user/${currentId}`;

    try {
      await axios.post(endpoint, payload);
      alert("신고가 정상적으로 접수되었습니다.");
      navigate(-1);
    } catch (err) {
      console.error(err);
      alert("신고 처리 중 오류가 발생했습니다. 콘솔을 확인해 주세요.");
    }
  };

  return (
    <div className="max-w-lg mx-auto p-6 bg-white rounded shadow-md">
      <h1 className="text-2xl font-bold mb-6">신고하기</h1>

      {/* step indicator */}
      <div className="flex mb-6 text-sm">
        {["대상 선택","상세 입력","제출"].map((label,i) => (
          <div
            key={i}
            className={`flex-1 text-center pb-2 border-b-2 ${
              step === i+1
                ? "border-purple-600 text-purple-600"
                : "border-gray-200 text-gray-500"
            }`}
          >
            {label}
          </div>
        ))}
      </div>

      {/* STEP 1 */}
      {step === 1 && (
        <div className="space-y-4">
          <label className="block font-medium">누구를 신고하나요?</label>
          <div className="flex gap-4">
            {["USER","COMPANY"].map(t => (
              <button
                key={t}
                type="button"
                className={`px-4 py-2 rounded ${
                  targetType === t
                    ? "bg-purple-600 text-white"
                    : "bg-gray-100 text-gray-700"
                }`}
                onClick={() => {
                  setTargetType(t);
                  setReportedId("");
                }}
              >
                {t === "USER" ? "사용자" : "업체"}
              </button>
            ))}
          </div>

          {targetType && (
            <AsyncSelect
              cacheOptions
              defaultOptions
              loadOptions={ targetType==="USER" ? loadUserOptions : loadCompanyOptions }
              onChange={opt => setReportedId(opt?.value||"")}
              placeholder={
                targetType==="USER"
                  ? "사용자 이메일/ID 검색…"
                  : "업체 이메일 검색…"
              }
              className="mt-2"
            />
          )}

          <div className="flex justify-end">
            <button
              type="button"
              disabled={!canNext1}
              onClick={()=>setStep(2)}
              className={`px-5 py-2 rounded ${
                canNext1
                  ? "bg-purple-600 text-white"
                  : "bg-gray-200 text-gray-500 cursor-not-allowed"
              }`}
            >
              다음
            </button>
          </div>
        </div>
      )}

      {/* STEP 2 */}
      {step === 2 && (
        <div className="space-y-4">
          <label className="block font-medium">카테고리</label>
          <select
            className="w-full bg-white border px-3 py-2 rounded text-gray-900"
            value={category}
            onChange={e => setCategory(e.target.value)}
          >
            <option value="" disabled>신고 카테고리 선택…</option>
            {CATEGORY_OPTIONS.map(o=>(
              <option key={o.value} value={o.value}>{o.label}</option>
            ))}
          </select>

          <label className="block font-medium">제목</label>
          <input
            type="text"
            placeholder="신고 제목을 입력하세요"
            maxLength={100}
            className="w-full bg-white border px-3 py-2 rounded text-gray-900"
            value={title}
            onChange={e=>setTitle(e.target.value)}
          />

          <label className="block font-medium">내용</label>
          <textarea
            rows={5}
            placeholder="신고 내용을 입력하세요"
            maxLength={1000}
            className="w-full bg-white border px-3 py-2 rounded resize-none text-gray-900"
            value={content}
            onChange={e=>setContent(e.target.value)}
          />

          <label className="block font-medium">이미지 업로드 (선택)</label>
          <input
            type="file"
            accept="image/*"
            multiple
            onChange={handleFileChange}
            className="block w-full text-sm text-gray-700 mt-1"
          />
          {files.length > 0 && (
            <ul className="list-disc list-inside text-sm text-gray-600 mt-2">
              {files.map((f,i)=><li key={i}>{f.name}</li>)}
            </ul>
          )}

          <div className="flex justify-between">
            <button
              type="button"
              onClick={()=>setStep(1)}
              className="px-5 py-2 rounded bg-gray-200 text-gray-700"
            >
              이전
            </button>
            <button
              type="button"
              disabled={!canNext2}
              onClick={()=>setStep(3)}
              className={`px-5 py-2 rounded ${
                canNext2
                  ? "bg-purple-600 text-white"
                  : "bg-gray-200 text-gray-500 cursor-not-allowed"
              }`}
            >
              다음
            </button>
          </div>
        </div>
      )}

      {/* STEP 3 (제출) */}
      {step === 3 && (
        <div className="space-y-4">
          <div>
            <strong>대상:</strong>{" "}
            {targetType
              ? (targetType === "USER" ? "사용자" : "업체")
              : <span className="text-gray-400">대상을 선택하세요</span>
            }
          </div>

          <div>
            <strong>ID:</strong>{" "}
            {reportedId
              ? reportedId
              : <span className="text-gray-400">ID를 선택하세요</span>
            }
          </div>

          <div>
            <strong>카테고리:</strong>{" "}
            {category
              ? CATEGORY_OPTIONS.find(o=>o.value===category)?.label
              : <span className="text-gray-400">신고 카테고리를 선택하세요</span>
            }
          </div>

          <div>
            <strong>제목:</strong>{" "}
            {title
              ? title
              : <span className="text-gray-400">신고 제목을 입력하세요</span>
            }
          </div>

          <div>
            <strong>내용:</strong>
            <p className="whitespace-pre-wrap border p-3 rounded bg-gray-50 text-gray-900">
              {content
                ? content
                : "신고 내용을 입력하세요"
              }
            </p>
          </div>

          {imageUrls.length > 0
            ? (
              <div>
                <strong>업로드된 이미지 URL:</strong>
                <ul className="list-disc list-inside">
                  {imageUrls.map((u,i)=><li key={i}>{u}</li>)}
                </ul>
              </div>
            )
            : <div className="text-gray-400">업로드된 이미지가 없습니다</div>
          }

          <div className="flex justify-between">
            <button
              type="button"
              onClick={()=>setStep(2)}
              className="px-5 py-2 rounded bg-gray-200 text-gray-700"
            >
              이전
            </button>
            <button
              type="button"
              onClick={handleSubmit}
              className="px-5 py-2 rounded bg-green-600 text-white"
            >
              제출하기
            </button>
          </div>
        </div>
      )}
    </div>
  );
}
