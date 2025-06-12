// src/pages/AdminReportListPage.jsx

import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

const BASE_URL = import.meta.env.VITE_API_BASE_URL;

export default function AdminReportListPage() {
  const [reports, setReports] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
  axios.get(`${BASE_URL}/report/admin`, { withCredentials: true })
    .then(res => setReports(res.data.data))
    .catch(err => console.error("신고 목록 조회 실패:", err));
  }, []);

  return (
    <div>
      <h2>신고 목록</h2>
      <table>
        <thead>
          <tr>
            <th>번호</th>
            <th>신고자</th>
            <th>대상</th>
            <th>제목</th>
            <th>업체ID</th>
            <th>유저ID</th>
            <th>신고 시간</th>
          </tr>
        </thead>
        <tbody>
        {reports.map(r => {
          console.log("backend data", r.cpId, r.userId, r.createdAt);
            return (
              <tr
                key={r.reportId}
                style={{ cursor: "pointer" }}
                onClick={() => navigate(`/report/report/${r.reportId}`)}
              >
                <td>{r.reportId}</td>
                <td>{r.reporterName}</td>
                <td>{r.targetName}</td>
                <td>{r.reportTitle}</td>
                <td>{r.cpId}</td>
                <td>{r.userId}</td>
                <td>{r.createdAt}</td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
}
