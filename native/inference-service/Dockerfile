# Build stage
FROM nvidia/cuda:12.4.1-devel-ubuntu22.04 AS builder

RUN apt-get update && apt-get install -y \
    cmake ninja-build git curl unzip \
    build-essential libprotobuf-dev protobuf-compiler \
    libgrpc++-dev protobuf-compiler-grpc \
    && rm -rf /var/lib/apt/lists/*

# Install ONNX Runtime (GPU build)
ARG ONNXRUNTIME_VERSION=1.20.1
RUN curl -LO https://github.com/microsoft/onnxruntime/releases/download/v${ONNXRUNTIME_VERSION}/onnxruntime-linux-x64-gpu-${ONNXRUNTIME_VERSION}.tgz \
    && tar -xzf onnxruntime-linux-x64-gpu-${ONNXRUNTIME_VERSION}.tgz -C /opt \
    && mv /opt/onnxruntime-linux-x64-gpu-${ONNXRUNTIME_VERSION} /opt/onnxruntime

COPY . /app
WORKDIR /app
RUN cmake -B build -G Ninja -DCMAKE_BUILD_TYPE=Release \
    -DONNXRuntime_ROOT_DIR=/opt/onnxruntime \
    && cmake --build build

# Runtime Stage
FROM nvidia/cuda:12.4.1-runtime-ubuntu22.04

RUN apt-get update && apt-get install -y \
    libprotobuf-dev libgrpc++-dev \
    && rm -rf /var/lib/apt/lists/*

COPY --from=builder /opt/onnxruntime /opt/onnxruntime
COPY --from=builder /app/build/inference_service /usr/local/bin/inference_service
COPY --from=builder /app/models /models

ENV LD_LIBRARY_PATH=/opt/onnxruntime/lib:$LD_LIBRARY_PATH
ENV MODEL_PATH=/models/detection.onnx

EXPOSE 50051
ENTRYPOINT ["/usr/local/bin/inference_service"]