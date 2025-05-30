import axios from "axios";
import humps from "humps";

const baseUrl = import.meta.env.VITE_API_BASE_URL;

const apiClient = axios.create({
    baseURL: baseUrl,
    withCredentials: true,
    headers: {
        "Content-Type": "application/json",
    },
    transformRequest: [(data, headers) => {
        if (data instanceof FormData) return data; 
        
        const snakeCaseData = humps.decamelizeKeys(data);
        return JSON.stringify(snakeCaseData);
    }],
    transformResponse: [(data) => {
        try {
            const parsed = JSON.parse(data);
            console.log("🔙 응답 원본 데이터:", parsed);
            return humps.camelizeKeys(parsed);
        } catch {
            return data;
        }
    }]
});

export default apiClient;
