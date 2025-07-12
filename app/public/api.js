const API_BASE="https://1e2b08e8a537.ngrok-free.app";

export async function post(path, data) {
    const response=await fetch(`${API_BASE}${path}`, {
        method: "POST",
        headers: {"Content-Type":"application/json"},
        body: JSON.stringify(data),
    });
    return await response.json();
}