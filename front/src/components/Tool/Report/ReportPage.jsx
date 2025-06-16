// src/components/Report/ReportPage.jsx
import React, { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import Call from "../../image/Support/call.png";
import axios from "axios";
import AsyncSelect from "react-select/async";
import { useAuth } from "../../../hooks/useAuth";

// always send your auth cookie/session on every request
axios.defaults.withCredentials = true;

const BASE_URL = import.meta.env.VITE_API_BASE_URL;

const CATEGORY_OPTIONS = [
  { value: "GENERAL", label: "일반 컨텐츠" },
  { value: "SPAM", label: "스팸" },
  { value: "FLAGGED", label: "선정성이 있는 컨텐츠" },
  { value: "BAD_SERVICE", label: "업체의 서비스가 좋지 않습니다." },
  { value: "RESTRICTED", label: "제한된 컨텐츠" },
];

export default function ReportPage() {
  const navigate = useNavigate();
  const { currentId } = useAuth();   // your logged-in companyId or userId

  const [cpId, setCpId] = useState("");
  const [userId, setUserId] = useState("");
  const [step, setStep] = useState(1);
  const [targetType, setTargetType] = useState("");   // "USER" or "COMPANY"
  const [reportedId, setReportedId] = useState("");   // the ID you’re reporting
  const [category, setCategory] = useState("");
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [files, setFiles] = useState([]);
  const [imageUrls, setImageUrls] = useState([]);
  const [targetName, setTargetName] = useState(""); //대상 이름름
  const [reporterName, setReporterName] = useState("");

  const menuItems = [
    { label: "고객지원", path: "/support" },
    { label: "챗봇", path: "/chatbot" },
    { label: "건의함", path: "/suggestion" },
    { label: "신고", path: "/report" },
  ];

  const canNext1 = Boolean(reportedId);
  const canNext2 = Boolean(category && title.trim() && content.trim());

  // ─── type-ahead loaders ────────────────────────────────
  const loadCompanyOptions = async input => {
    if (!input) return [];
    try {
      const { data } = await axios.get(
        `${BASE_URL}/company/search/${encodeURIComponent(input)}`,
        { withCredentials: true }
      );
      const c = data.data;
      console.log("신고할 업체 정보 : ", c);
      console.log("신고할 업체 이메일 : ", c.email);
      console.log("신고할 업체 id : ", c.id);
      setCpId(c.id);
      return [{ value: String(c.cpId), label: c.cpName }];
    } catch {
      return [];
    }

  };

  const loadUserOptions = async (input) => {
    if (!input) return [];
    try {
      const { data } = await axios.get(
        `${BASE_URL}/user/profile/email/${encodeURIComponent(input)}`,
        { withCredentials: true }
      );
      const u = data.data;
      console.log("신고할 사용자 정보:", u);
      console.log("신고할 사용자 id:", u.userId);
      if (!u) return [];
      setUserId(u.userId);
      return [{ value: u.userId, label: u.name }];
    } catch (error) {
      console.error(error); // Optional: for debugging
      return [];
    }
  };
  // ────────────────────────────────────────────────────────

  // immediately upload your selected files
  // const handleFileChange = e => {
  //   const chosen = Array.from(e.target.files || []);
  //   setFiles(chosen);
  //   setImageUrls([]);
  //   // **use the actor’s ID** in the path, _not_ `reportedId`:
  //   const uploadUrl =
  //     targetType === "USER"
  //       ? `${BASE_URL}/report/company/image/upload`
  //       : `${BASE_URL}/report/user/image/upload`;

  //   const uploaded = [];
  //   for (let file of chosen) {
  //     const form = new FormData();
  //     form.append("images", file);
  //     try {
  //       const res =  axios.post(uploadUrl, form, {
  //         headers: { "Content-Type": "multipart/form-data" }
  //       });
  //       // our API returns { data: [ ...urls ] }
  //       uploaded.push(...res.data.data);
  //     } catch (err) {
  //       console.error("upload failed:", err);
  //       alert(`${file.name} 업로드에 실패했습니다.`);
  //     }
  //   }
  //   console.log("신고업체 ID", reportedId);
  //   setImageUrls(uploaded);
  // };
  const handleFileChange = e => {
    const chosen = Array.from(e.target.files || []);
    setFiles(chosen);
    setImageUrls([]);
  };

  const handleSubmit = async () => {
    const reportPayload = {
      reportTitle: title,
      reportContent: content,
      reportCategory: category,
      reportTarget: targetType === "USER" ? "U" : "C",
      targetName,
      reporterName,
      ...(targetType === "COMPANY"
        ? { cpId: Number(cpId) }
        : { userId: reportedId }),
    }
    const endpoint = targetType === "USER"
      ? `${BASE_URL}/report/company`
      : `${BASE_URL}/report/user`;

    try {
      const res = await axios.post(endpoint, reportPayload, { withCredentials: true });
      const reportId = res.data.data.reportId;


      if (files.length > 0) {
        const uploadUrl = targetType === "USER"
          ? `${BASE_URL}/report/company/image/upload`
          : `${BASE_URL}/report/user/image/upload`;

        const form = new FormData();
        files.forEach(file => form.append("images", file));
        form.append(
          "request",
          new Blob([JSON.stringify({ reportId })], { type: "application/json" })
        );

        const uploadRes = await axios.post(uploadUrl, form, {
          headers: { "Content-Type": "multipart/form-data" },
          withCredentials: true
        });

        setImageUrls(uploadRes.data.data);
      }

      alert("신고가 정상적으로 접수되었습니다.");
      navigate("/support");
    } catch (err) {
      console.error(err);
      alert("신고 처리 중 오류가 발생했습니다.");
    }
  };

  useEffect(() => {
    if (reportedId) {
      console.log("reportedId가 업데이트되었습니다:", reportedId);
    }
  }, [reportedId]);

  return (

    <div className="mx-auto w-full border-[1px] font-semibold border-[#845EC2]">
      {/* 탭 메뉴 */}
      <ul className="flex list-none m-0 p-0 border-b border-[#e1c2ff33]">
        {menuItems.map((item) => (
          <li
            key={item.label}
            className="flex-1 text-center h-[60px] border-r last:border-r-0 border-[#e1c2ff33]"
          >
            <Link
              to={item.path}
              className={`flex items-center justify-center w-full h-full text-[18px] font-semibold
                              ${item.label === "신고" ? "bg-[#E2C5EE] text-black" : "text-[#615e5e]"}
                              ${item.label === "신고" ? "bg-black-100 text-black-500" : ""}
                              hover:bg-[#E2C5EE] hover:text-black`}
            >
              {item.label}
            </Link>
          </li>
        ))}
      </ul>

      <div className="max-w-lg mx-auto p-6 bg-white rounded shadow-md">

        <h1 className="text-2xl font-bold mb-6">신고하기</h1>

        {/* step indicator */}
        <div className="flex mb-6 text-sm">
          {["대상 선택", "상세 입력", "제출"].map((lab, i) => (
            <div
              key={i}
              className={`flex-1 text-center pb-2 border-b-2 ${step === i + 1
                  ? "border-purple-600 text-purple-600"
                  : "border-gray-200 text-gray-500"
                }`}
            >{lab}</div>
          ))}
        </div>

        {/* STEP 1 */}
        {step === 1 && (
          <div className="space-y-4">
            <label className="block font-medium mt-4">신고 대상 이름</label>
            <input
              type="text"
              placeholder="신고 대상 이름을 입력하세요"
              className="w-full bg-white border px-3 py-2 rounded text-gray-900"
              value={targetName}
              onChange={e => setTargetName(e.target.value)}
            />


            <label className="block font-medium mt-4">신고자 이름</label>
            <input
              type="text"
              placeholder="신고자 이름을 입력하세요"
              className="w-full bg-white border px-3 py-2 rounded text-gray-900"
              value={reporterName}
              onChange={e => setReporterName(e.target.value)}

            />


            <label className="block font-medium">누구를 신고하나요?</label>
            <div className="flex gap-4">
              {["USER", "COMPANY"].map(t => (
                <button
                  key={t}
                  type="button"
                  className={`px-4 py-2 rounded ${targetType === t
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
                loadOptions={targetType === "USER" ? loadUserOptions : loadCompanyOptions}
                onChange={opt => {
                  console.log("Selected option:", opt);
                  setReportedId(opt?.value || "");
                }}
                placeholder={
                  targetType === "USER"
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
                onClick={() => setStep(2)}
                className={`px-5 py-2 rounded ${canNext1
                    ? "bg-purple-600 text-white"
                    : "bg-gray-200 text-gray-500 cursor-not-allowed"
                  }`}
              >다음</button>
            </div>
          </div>
        )}

        {/* STEP 2 */}
        {step === 2 && (
          <div className="space-y-4">
            {/* <label className="block font-medium mt-4">신고 대상 이름</label>
            <input
              type="text"
              placeholder="신고 대상 이름을 입력하세요"
              className="w-full bg-white border px-3 py-2 rounded text-gray-900"
              value={reportedName}
              onChange={e => setReportedName(e.target.value)}
            /> */}
            <label className="block font-medium">카테고리</label>
            <select
              className="w-full bg-white border px-3 py-2 rounded text-gray-900"
              value={category}
              onChange={e => setCategory(e.target.value)}
            >
              <option value="" disabled>신고 카테고리 선택…</option>
              {CATEGORY_OPTIONS.map(o => (
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
              onChange={e => setTitle(e.target.value)}
            />

            <label className="block font-medium">내용</label>
            <textarea
              rows={5}
              placeholder="신고 내용을 입력하세요"
              maxLength={1000}
              className="w-full bg-white border px-3 py-2 rounded resize-none text-gray-900"
              value={content}
              onChange={e => setContent(e.target.value)}
            />

            <label className="block font-medium">이미지 업로드 (선택)</label>
            <input
              type="file"
              multiple
              accept="image/*"
              onChange={handleFileChange}
              className="block w-full text-sm text-gray-700 mt-1"
            />
            {files.length > 0 && (
              <ul className="list-disc list-inside text-sm text-gray-600 mt-2">
                {files.map((f, i) => <li key={i}>{f.name}</li>)}
              </ul>
            )}

            <div className="flex justify-between">
              <button
                type="button"
                onClick={() => setStep(1)}
                className="px-5 py-2 rounded bg-gray-200 text-gray-700"
              >이전</button>
              <button
                type="button"
                disabled={!canNext2}
                onClick={() => setStep(3)}
                className={`px-5 py-2 rounded ${canNext2
                    ? "bg-purple-600 text-white"
                    : "bg-gray-200 text-gray-500 cursor-not-allowed"
                  }`}
              >다음</button>
            </div>
          </div>
        )}

        {/* STEP 3 */}
        {step === 3 && (
          <div className="space-y-4">
            <div><strong>대상:</strong> {targetType === "USER" ? "사용자" : "업체"}</div>
            <div><strong>ID:</strong> {reportedId}</div>
            <div>
              <strong>카테고리:</strong>{" "}
              {CATEGORY_OPTIONS.find(o => o.value === category)?.label}
            </div>
            <div><strong>제목:</strong> {title}</div>
            <div>
              <strong>내용:</strong>
              <p className="whitespace-pre-wrap border p-3 rounded bg-gray-50 text-gray-900">
                {content}
              </p>
            </div>
            {imageUrls.length > 0 ? (
              <div>
                <strong>업로드된 이미지 URL:</strong>
                <ul className="list-disc list-inside">
                  {imageUrls.map((u, i) => <li key={i}>{u}</li>)}
                </ul>
              </div>
            ) : (
              <div className="text-gray-400">이미지 업로드가 되었습니다다</div>
            )}

            <div className="flex justify-between">
              <button
                type="button"
                onClick={() => setStep(2)}
                className="px-5 py-2 rounded bg-gray-200 text-gray-700"
              >이전</button>
              <button
                type="button"
                onClick={handleSubmit}
                className="px-5 py-2 rounded bg-green-600 text-white"
              >제출하기</button>
            </div>
          </div>

        )}
      </div>
      {/* 하단 고객센터 안내 */}
      <div className="w-full border-t-2 border-[#845EC2] bg-[#e1c2ff66] text-center py-5">
        <div className="text-[20px] font-bold">고객센터 이용안내</div>
        <div className="flex items-center justify-center gap-2 text-[16px] font-bold mt-1">
          <img src={Call} alt="Call Icon" className="w-6 h-6" />
          월~금 10:00~18:00
        </div>
        <div className="mt-1 text-[14px] text-gray-600 font-bold">(점심 12:00~13:00)</div>
      </div>
    </div>
  );
}