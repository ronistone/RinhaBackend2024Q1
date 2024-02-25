FROM golang:1.22.0-alpine3.19 as builder
RUN apk add --no-cache git
WORKDIR /app
COPY . .
RUN go mod download
RUN go build -o /go/bin/app .

FROM alpine:3.13
WORKDIR /app
COPY --from=builder /go/bin/app /go/bin/app
CMD ["/go/bin/app"]
