terraform {
  required_providers {
    aws = { source = "hashicorp/aws", version = "5.31.0" }
  }
}

provider "aws" {
  profile = "default"
  region  = "us-east-1"
}

data "aws_availability_zones" "available" {
  state = "available"
}

locals {
  zones_count = 2
  zones_names = data.aws_availability_zones.available.names
}

resource "aws_vpc" "main-vpc" {
  cidr_block           = "10.10.0.0/16"
  enable_dns_hostnames = true
  enable_dns_support   = true
  tags                 = { Name = "main-vpc" }
}

resource "aws_subnet" "public" {
  count                   = local.zones_count
  vpc_id                  = aws_vpc.main-vpc.id
  availability_zone       = local.zones_names[count.index]
  cidr_block              = cidrsubnet(aws_vpc.main-vpc.cidr_block, 8, 10 + count.index)
  map_public_ip_on_launch = true
  tags                    = { Name = "main-public-${local.zones_names[count.index]}" }
}