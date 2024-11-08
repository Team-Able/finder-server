package com.finder.domain.comment.exception.impl;

import com.finder.domain.comment.exception.CommentException;
import com.finder.global.exception.CustomException;

public class ParentCommentNotFound extends CustomException {
  private ParentCommentNotFound() {super(CommentException.Parent_Comment_Not_Found);}

  public static final ParentCommentNotFound INSTANCE = new ParentCommentNotFound();
}
