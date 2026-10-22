cmake_minimum_required(VERSION 3.24)
project(aegis_inference CXX)

set(CMAKE_CXX_STANDARD 20)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

find_package(Protobuf REQUIRED)
find_package(gRPC CONFIG REQUIRED)
find_package(ONNXRuntime REQUIRED)

# Generate gRPC/protobuf C++ sources
add_executable(inference_service
    src/main.cpp
    src/inference_service.cpp
    ${CMAKE_BINARY_DIR}/contracts/inference.pb.cc
    ${CMAKE_BINARY_DIR}/contracts/inference.grpc.pb.cc
)

target_include_directories(inference_service PRIVATE
    ${CMAKE_BINARY_DIR}/contracts
    src
)

target_link_libraries(inference_service PRIVATE
    gRPC::grpc++
    protobuf::libprotobuf
    onnxruntime
)