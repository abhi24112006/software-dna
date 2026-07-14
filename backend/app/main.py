from fastapi import FastAPI

app = FastAPI(
    title="Software DNA API",
    version="0.1.0",
    description="Backend API for Software DNA"
)


@app.get(
    "/",
    tags=["Health Check"],
    summary="Check if the backend is running"
)
async def root():
    return {
        "status": "running",
        "project": "Software DNA",
        "version": "0.1.0",
        "message": "Backend is running successfully 🚀"
    }