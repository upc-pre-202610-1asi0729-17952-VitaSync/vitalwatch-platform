# VitalWatch Platform - REST API Technical Stories

## Overview

Base path: `/api/v1`

This document will contain API-focused technical stories for frontend or mobile developers integrating with the VitalWatch Platform REST API.

## Common conventions

- All request and response bodies use `Content-Type: application/json`.
- Error responses follow a standard schema:
  - `code`
  - `message`
  - `details`
- Localized errors support:
  - `en`
  - `es`
