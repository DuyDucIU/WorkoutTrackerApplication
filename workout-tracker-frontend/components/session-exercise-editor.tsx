'use client';

import { useState, useEffect, useCallback } from 'react';
import { Exercise, SessionExerciseInput, exercisesApi } from '@/lib/api';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Plus, Trash2, ChevronUp, ChevronDown, Loader2, Dumbbell } from 'lucide-react';

interface SessionExerciseEditorProps {
  exercises: SessionExerciseInput[];
  onChange: (exercises: SessionExerciseInput[]) => void;
}

export default function SessionExerciseEditor({
  exercises,
  onChange,
}: SessionExerciseEditorProps) {
  const [availableExercises, setAvailableExercises] = useState<Exercise[]>([]);
  const [isLoadingExercises, setIsLoadingExercises] = useState(true);

  const fetchExercises = useCallback(async () => {
    try {
      const response = await exercisesApi.getAll();
      setAvailableExercises(response.data);
    } catch {
      // Error handled by interceptor
    } finally {
      setIsLoadingExercises(false);
    }
  }, []);

  useEffect(() => {
    fetchExercises();
  }, [fetchExercises]);

  const addExercise = () => {
    const newExercise: SessionExerciseInput = {
      exerciseId: 0,
      sets: undefined,
      reps: undefined,
      weight: undefined,
      duration: undefined,
      orderIndex: exercises.length,
    };
    onChange([...exercises, newExercise]);
  };

  const removeExercise = (index: number) => {
    const updated = exercises.filter((_, i) => i !== index);
    // Reindex
    const reindexed = updated.map((ex, i) => ({ ...ex, orderIndex: i }));
    onChange(reindexed);
  };

  const updateExercise = (index: number, field: keyof SessionExerciseInput, value: number | undefined) => {
    const updated = [...exercises];
    updated[index] = { ...updated[index], [field]: value };
    onChange(updated);
  };

  const moveExercise = (index: number, direction: 'up' | 'down') => {
    if (
      (direction === 'up' && index === 0) ||
      (direction === 'down' && index === exercises.length - 1)
    ) {
      return;
    }

    const updated = [...exercises];
    const newIndex = direction === 'up' ? index - 1 : index + 1;
    [updated[index], updated[newIndex]] = [updated[newIndex], updated[index]];
    
    // Update orderIndex
    const reindexed = updated.map((ex, i) => ({ ...ex, orderIndex: i }));
    onChange(reindexed);
  };

  const getExerciseName = (exerciseId: number) => {
    const exercise = availableExercises.find((e) => e.id === exerciseId);
    return exercise?.name || 'Select exercise';
  };

  const getExerciseCategory = (exerciseId: number) => {
    const exercise = availableExercises.find((e) => e.id === exerciseId);
    return exercise?.category || '';
  };

  const validateNumber = (value: string): number | undefined => {
    if (value === '') return undefined;
    const num = parseInt(value, 10);
    if (isNaN(num) || num < 0) return undefined;
    return num;
  };

  if (isLoadingExercises) {
    return (
      <div className="flex items-center justify-center py-8">
        <Loader2 className="h-6 w-6 animate-spin text-primary" />
      </div>
    );
  }

  return (
    <div className="space-y-4">
      <div className="flex items-center justify-between">
        <Label className="text-lg font-semibold text-foreground">Exercises</Label>
        <Button type="button" variant="outline" size="sm" onClick={addExercise} className="gap-2 bg-transparent">
          <Plus className="h-4 w-4" />
          Add Exercise
        </Button>
      </div>

      {exercises.length === 0 ? (
        <div className="flex flex-col items-center justify-center py-12 text-center border border-dashed border-border rounded-lg">
          <Dumbbell className="h-10 w-10 text-muted-foreground mb-3" />
          <p className="text-muted-foreground mb-3">No exercises added yet</p>
          <Button type="button" variant="outline" size="sm" onClick={addExercise} className="gap-2 bg-transparent">
            <Plus className="h-4 w-4" />
            Add Your First Exercise
          </Button>
        </div>
      ) : (
        <div className="space-y-3">
          {exercises.map((exercise, index) => (
            <Card key={index} className="bg-card border-border">
              <CardHeader className="py-3 px-4">
                <div className="flex items-center justify-between">
                  <CardTitle className="text-sm font-medium text-card-foreground flex items-center gap-2">
                    <span className="bg-primary text-primary-foreground w-6 h-6 rounded-full flex items-center justify-center text-xs">
                      {index + 1}
                    </span>
                    {getExerciseName(exercise.exerciseId)}
                    {getExerciseCategory(exercise.exerciseId) && (
                      <span className="text-xs text-muted-foreground font-normal">
                        ({getExerciseCategory(exercise.exerciseId)})
                      </span>
                    )}
                  </CardTitle>
                  <div className="flex items-center gap-1">
                    <Button
                      type="button"
                      variant="ghost"
                      size="icon"
                      onClick={() => moveExercise(index, 'up')}
                      disabled={index === 0}
                      className="h-8 w-8"
                    >
                      <ChevronUp className="h-4 w-4" />
                      <span className="sr-only">Move up</span>
                    </Button>
                    <Button
                      type="button"
                      variant="ghost"
                      size="icon"
                      onClick={() => moveExercise(index, 'down')}
                      disabled={index === exercises.length - 1}
                      className="h-8 w-8"
                    >
                      <ChevronDown className="h-4 w-4" />
                      <span className="sr-only">Move down</span>
                    </Button>
                    <Button
                      type="button"
                      variant="ghost"
                      size="icon"
                      onClick={() => removeExercise(index)}
                      className="h-8 w-8 text-destructive hover:text-destructive hover:bg-destructive/10"
                    >
                      <Trash2 className="h-4 w-4" />
                      <span className="sr-only">Remove</span>
                    </Button>
                  </div>
                </div>
              </CardHeader>
              <CardContent className="py-3 px-4 pt-0">
                <div className="grid gap-3 sm:grid-cols-5">
                  <div className="sm:col-span-1">
                    <Label className="text-xs text-muted-foreground">Exercise</Label>
                    <Select
                      value={exercise.exerciseId ? exercise.exerciseId.toString() : ''}
                      onValueChange={(v) => updateExercise(index, 'exerciseId', parseInt(v, 10))}
                    >
                      <SelectTrigger className="bg-background border-input mt-1">
                        <SelectValue placeholder="Select" />
                      </SelectTrigger>
                      <SelectContent>
                        {availableExercises.map((ex) => (
                          <SelectItem key={ex.id} value={ex.id.toString()}>
                            {ex.name}
                          </SelectItem>
                        ))}
                      </SelectContent>
                    </Select>
                  </div>
                  <div>
                    <Label className="text-xs text-muted-foreground">Sets</Label>
                    <Input
                      type="number"
                      min="0"
                      placeholder="0"
                      value={exercise.sets ?? ''}
                      onChange={(e) => updateExercise(index, 'sets', validateNumber(e.target.value))}
                      className="bg-background border-input mt-1"
                    />
                  </div>
                  <div>
                    <Label className="text-xs text-muted-foreground">Reps</Label>
                    <Input
                      type="number"
                      min="0"
                      placeholder="0"
                      value={exercise.reps ?? ''}
                      onChange={(e) => updateExercise(index, 'reps', validateNumber(e.target.value))}
                      className="bg-background border-input mt-1"
                    />
                  </div>
                  <div>
                    <Label className="text-xs text-muted-foreground">Weight (kg)</Label>
                    <Input
                      type="number"
                      min="0"
                      placeholder="0"
                      value={exercise.weight ?? ''}
                      onChange={(e) => updateExercise(index, 'weight', validateNumber(e.target.value))}
                      className="bg-background border-input mt-1"
                    />
                  </div>
                  <div>
                    <Label className="text-xs text-muted-foreground">Duration (min)</Label>
                    <Input
                      type="number"
                      min="0"
                      placeholder="0"
                      value={exercise.duration ?? ''}
                      onChange={(e) => updateExercise(index, 'duration', validateNumber(e.target.value))}
                      className="bg-background border-input mt-1"
                    />
                  </div>
                </div>
              </CardContent>
            </Card>
          ))}
        </div>
      )}
    </div>
  );
}
